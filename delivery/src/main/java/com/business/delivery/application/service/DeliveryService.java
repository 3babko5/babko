package com.business.delivery.application.service;

import com.business.common.application.exception.BusinessLogicException;
import com.business.common.infrastructure.api.NaverApiService;
import com.business.delivery.application.dto.mapper.DeliveryResponseMapper;
import com.business.delivery.application.dto.mapper.DeliveryRequestMapper;
import com.business.delivery.application.dto.request.CreateDeliveryRequestDto;
import com.business.delivery.application.dto.request.SearchRequestDto;
import com.business.delivery.application.dto.request.StatusUpdateRequestDto;
import com.business.delivery.application.dto.response.DeliveryDetailResponseDto;
import com.business.delivery.application.dto.response.DeliveryPageResponseDto;
import com.business.delivery.application.dto.response.DeliveryResponseDto;
import com.business.delivery.application.dto.response.DeliveryStatusUpdateResponseDto;
import com.business.delivery.application.exception.DeliveryErrorCode;
import com.business.delivery.domain.entity.Delivery;
import com.business.delivery.domain.entity.DeliveryRoute;
import com.business.delivery.domain.entity.DeliveryRouteStatus;
import com.business.delivery.domain.entity.DeliveryStatus;
import com.business.delivery.domain.repository.DeliveryRepository;
import com.business.delivery.infrastructure.client.HubClient;
import com.business.delivery.infrastructure.client.OrderClient;
import com.business.delivery.infrastructure.client.UserClient;
import com.business.delivery.infrastructure.dto.mapper.HubMapper;
import com.business.delivery.infrastructure.dto.response.HubIdResponseDto;
import com.business.delivery.infrastructure.dto.response.HubRoutesResponseDto;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final HubClient hubClient;
    private final NaverApiService naverApiService;
    private final UserClient userClient;
    private final OrderClient orderClient;

    public DeliveryResponseDto createDelivery(CreateDeliveryRequestDto request, Long userId, String role) {

        if (userId == null) {
            throw new BusinessLogicException(DeliveryErrorCode.INVALID_USER_ID);
        }

        if (request.getStartHubId() == null || request.getEndHubId() == null) {
            throw new BusinessLogicException(DeliveryErrorCode.INVALID_HUB_ID);
        }
        if (request.getDeliveryAddress() == null || request.getDeliveryAddress().isEmpty()) {
            throw new BusinessLogicException(DeliveryErrorCode.INVALID_DELIVERY_ADDRESS);
        }

        List<HubRoutesResponseDto> hubMovements = Optional
            .ofNullable(hubClient.getRoutesByStartAndEndHub(request.getStartHubId(), request.getEndHubId(), userId, role))
            .filter(list -> !list.isEmpty())
            .orElseThrow(() -> new BusinessLogicException(DeliveryErrorCode.HUB_MOVEMENT_NOT_AVAILABLE));

        List<DeliveryRoute> hubDeliveryRoutes =
            IntStream.range(0, hubMovements.size())
                .mapToObj(i -> HubMapper.toEntityFromHubMovement(hubMovements.get(i), (long) i + 1))
                .toList();

        HubIdResponseDto hubInfo = hubClient.getLatitudeAndLongitude(request.getEndHubId(), userId, role);
        if (hubInfo == null || hubInfo.getLatitude() == null || hubInfo.getLongitude() == null) {
            throw new BusinessLogicException(DeliveryErrorCode.HUB_COORDINATE_NOT_AVAILABLE);
        }

        BigDecimal hubIdLatitude = hubInfo.getLatitude();
        BigDecimal hubIdlongitude = hubInfo.getLongitude();

        double[] deliveryCoords = naverApiService.getCoordinates(request.getDeliveryAddress());
        if (deliveryCoords == null || deliveryCoords.length < 2) {
            throw new BusinessLogicException(DeliveryErrorCode.INVALID_DELIVERY_COORDINATES);
        }

        String naverJsonResponse = naverApiService.getOptimalRoute(
            hubIdLatitude.doubleValue(), hubIdlongitude.doubleValue(),
            deliveryCoords[0], deliveryCoords[1]
        );

        if (naverJsonResponse == null || naverJsonResponse.isEmpty()) {
            throw new BusinessLogicException(DeliveryErrorCode.NAVER_ROUTE_NOT_AVAILABLE);
        }

        BigDecimal finalEstimatedDistance = naverApiService.extractDistanceFromJson(naverJsonResponse);
        if (finalEstimatedDistance == null || finalEstimatedDistance.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessLogicException(DeliveryErrorCode.INVALID_DISTANCE);
        }

        int durationInSeconds = naverApiService.extractDurationFromJson(naverJsonResponse);

        if (durationInSeconds <= 0) {
            throw new BusinessLogicException(DeliveryErrorCode.INVALID_DURATION);
        }

        Long finalEstimatedTime = (long) naverApiService.extractDurationFromJson(naverJsonResponse);

        DeliveryRoute finalRoute = DeliveryRequestMapper.toFinalRouteEntity(
            request.getEndHubId(),
            finalEstimatedDistance,
            finalEstimatedTime,
            hubDeliveryRoutes.size(),
            request.getDeliveryAddress()
        );

        List<DeliveryRoute> allRoutes =
            DeliveryRequestMapper.combineRoutes(hubDeliveryRoutes, finalRoute);

        Delivery delivery = DeliveryRequestMapper.createDeliveryRequestDtoToEntity(request);

        delivery.addDeliveryRoute(allRoutes);

        Delivery savedDelivery = deliveryRepository.saveAndFlush(delivery);

        assignDeliveryDriver(savedDelivery.getDeliveryId(), userId, role);

        return DeliveryResponseMapper.deliveryToDeliveryResponseDto(savedDelivery);
    }

    private void assignDeliveryDriver(UUID deliveryId, Long userId, String role) {

        userClient.assignDeliveryDriver(deliveryId, userId, role);

    }

    @Transactional(readOnly = true)
    public Page<DeliveryPageResponseDto> getDeliveries(SearchRequestDto request, Long userId, String role) {

        if (userId == null) {
            throw new BusinessLogicException(DeliveryErrorCode.INVALID_USER_ID);
        }

        Pageable pageable = DeliveryRequestMapper.deliverySearchRequestDtoToPageable(request);

        Page<Delivery> deliveryPage = deliveryRepository.findDeliveries(request, pageable);

        Page<DeliveryPageResponseDto> responsePage = deliveryPage.map(DeliveryResponseMapper::deliveryToPageResponseDto);

        return responsePage;
    }

    public DeliveryDetailResponseDto getDeliveryByDeliveryId(UUID deliveryId, Long userId, String role) {

        if (userId == null) {
            throw new BusinessLogicException(DeliveryErrorCode.INVALID_USER_ID);
        }

        Delivery delivery = deliveryRepository.findByDeliveryId(deliveryId);

        if (delivery == null) {
            throw new BusinessLogicException(DeliveryErrorCode.DELIVERY_NOT_FOUND);
        }

        Map<String, Object> orderResponse = orderClient.getOrder(delivery.getOrderId(), userId, role);

        return DeliveryResponseMapper.deliveryAndOrderToDetailResponse(delivery, orderResponse);
    }

    @Transactional
    public DeliveryStatusUpdateResponseDto updateDeliveryStatus(UUID deliveryId, StatusUpdateRequestDto request, Long userId, String role) {

        if (userId == null) {
            throw new BusinessLogicException(DeliveryErrorCode.INVALID_USER_ID);
        }

        Delivery delivery = deliveryRepository.findByDeliveryId(deliveryId);

        if (delivery == null) {
            throw new BusinessLogicException(DeliveryErrorCode.DELIVERY_NOT_FOUND);
        }

        DeliveryRoute deliveryRoute = delivery.getDeliveryRoutes().stream()
            .filter(route -> route.getDeliveryRouteStatus() != DeliveryRouteStatus.DELIVERED
                && route.getDeliveryRouteStatus() != DeliveryRouteStatus.CANCELED)
            .findFirst()
            .orElseThrow(() -> new BusinessLogicException(DeliveryErrorCode.DELIVERY_ROUTE_NOT_FOUND));

        DeliveryRoute lastRoute = delivery.getDeliveryRoutes().stream()
            .max(Comparator.comparing(DeliveryRoute::getRouteSequence))
            .orElseThrow(() -> new BusinessLogicException(DeliveryErrorCode.DELIVERY_ROUTE_NOT_FOUND));

        delivery.updateStatus(request.getDeliveryStatus());
        deliveryRoute.updateStatus(request.getDeliveryRouteStatus());

        Delivery updatedDelivery = deliveryRepository.save(delivery);

        if (lastRoute != null && lastRoute.getDeliveryRouteStatus() == DeliveryRouteStatus.DELIVERED) {
            updatedDelivery.updateStatus(DeliveryStatus.DELIVERED);
            updatedDelivery = deliveryRepository.save(updatedDelivery);

            orderClient.completeOrder(updatedDelivery.getOrderId(), userId, role);
        }

        userClient.updateDriverStatus(deliveryRoute.getDeliveryRouteId(), userId, role);

        return DeliveryResponseMapper.toStatusUpdateResponse(updatedDelivery, deliveryRoute);
    }

    @Transactional
    public void cancelDelivery(UUID deliveryId, Long userId, String role) {

        if (userId == null) {
            throw new BusinessLogicException(DeliveryErrorCode.INVALID_USER_ID);
        }

        Delivery delivery = deliveryRepository.findByDeliveryId(deliveryId);

        if (delivery == null) {
            throw new BusinessLogicException(DeliveryErrorCode.DELIVERY_NOT_FOUND);
        }

        delivery.updateCancelStatus();

        deliveryRepository.save(delivery);

        UUID deliveryRouteId = delivery.getDeliveryRoutes().stream()
            .filter(route -> route.getDeliveryRouteId() != null)
            .findFirst()
            .map(DeliveryRoute::getDeliveryRouteId)
            .orElseThrow(() -> new BusinessLogicException(DeliveryErrorCode.DELIVERY_ROUTE_NOT_FOUND));

        try {
            userClient.cancelDriverStatus(deliveryRouteId, userId, role);
        } catch (Exception e) {
            throw new BusinessLogicException(DeliveryErrorCode.DRIVER_CANCEL_ERROR);
        }
    }

    @Transactional
    public void deleteByDeliveryId(UUID deliveryId, Long deletedBy, Long userId, String role) {

        if (userId == null) {
            throw new BusinessLogicException(DeliveryErrorCode.INVALID_USER_ID);
        }

        deliveryRepository.deleteByDeliveryId(deliveryId, deletedBy);
    }
}