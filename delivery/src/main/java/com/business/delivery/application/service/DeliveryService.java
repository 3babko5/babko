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

    public DeliveryResponseDto createDelivery(CreateDeliveryRequestDto request) {

        List<HubRoutesResponseDto> hubMovements = Optional
            .ofNullable(hubClient.getRoutesByStartAndEndHub(request.getStartHubId(), request.getEndHubId()))
            .filter(list -> !list.isEmpty())
            .orElseThrow(() -> new BusinessLogicException(DeliveryErrorCode.HUB_MOVEMENT_NOT_AVAILABLE));

        List<DeliveryRoute> hubDeliveryRoutes =
            IntStream.range(0, hubMovements.size())
                .mapToObj(i -> HubMapper.toEntityFromHubMovement(hubMovements.get(i), (long) i + 1))
                .toList();

        HubIdResponseDto hubInfo = Optional
            .ofNullable(hubClient.getLatitudeAndLongitude(request.getEndHubId()))
            .orElseThrow(() -> new BusinessLogicException(DeliveryErrorCode.HUB_COORDINATE_NOT_AVAILABLE));

        BigDecimal hubIdLatitude = hubInfo.getLatitude();
        BigDecimal hubIdlongitude = hubInfo.getLongitude();

        double[] deliveryCoords = naverApiService.getCoordinates(request.getDeliveryAddress());

        String naverJsonResponse = naverApiService.getOptimalRoute(
            hubIdLatitude.doubleValue(), hubIdlongitude.doubleValue(),
            deliveryCoords[0], deliveryCoords[1]
        );

        BigDecimal finalEstimatedDistance = naverApiService.extractDistanceFromJson(naverJsonResponse);
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

        assignDeliveryDriver(savedDelivery.getDeliveryId());

        return DeliveryResponseMapper.deliveryToDeliveryResponseDto(savedDelivery);
    }

    private void assignDeliveryDriver(UUID deliveryId) {

        userClient.assignDeliveryDriver(deliveryId);
    }

    @Transactional(readOnly = true)
    public Page<DeliveryPageResponseDto> getDeliveries(SearchRequestDto request) {

        Pageable pageable = DeliveryRequestMapper.deliverySearchRequestDtoToPageable(request);

        Page<Delivery> deliveryPage = deliveryRepository.findDeliveries(request, pageable);

        Page<DeliveryPageResponseDto> responsePage = deliveryPage.map(DeliveryResponseMapper::deliveryToPageResponseDto);

        return responsePage;
    }

    public DeliveryDetailResponseDto getDeliveryByDeliveryId(UUID deliveryId) {

        Delivery delivery = deliveryRepository.findByDeliveryId(deliveryId);

        Map<String, Object> orderResponse = orderClient.getOrder(delivery.getOrderId());

        return DeliveryResponseMapper.deliveryAndOrderToDetailResponse(delivery, orderResponse);
    }

    @Transactional
    public DeliveryStatusUpdateResponseDto updateDeliveryStatus(UUID deliveryId, StatusUpdateRequestDto request) {

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

            orderClient.completeOrder(updatedDelivery.getOrderId());
        }

        userClient.updateDriverStatus(deliveryRoute.getDeliveryRouteId());

        return DeliveryResponseMapper.toStatusUpdateResponse(updatedDelivery, deliveryRoute);
    }

    @Transactional
    public void cancelDelivery(UUID deliveryId) {

        Delivery delivery = deliveryRepository.findByDeliveryId(deliveryId);

        delivery.updateCancelStatus();

        deliveryRepository.save(delivery);

        UUID deliveryRouteId = delivery.getDeliveryRoutes().stream()
            .filter(route -> route.getDeliveryRouteId() != null)
            .findFirst()
            .map(DeliveryRoute::getDeliveryRouteId)
            .orElseThrow(() -> new BusinessLogicException(DeliveryErrorCode.DELIVERY_ROUTE_NOT_FOUND));

        try {
            userClient.cancelDriverStatus(deliveryRouteId);
        } catch (Exception e) {
            throw new BusinessLogicException(DeliveryErrorCode.DRIVER_CANCEL_ERROR);
        }
    }

    @Transactional
    public void deleteByDeliveryId(UUID deliveryId, Long deletedBy) {

        deliveryRepository.deleteByDeliveryId(deliveryId, deletedBy);
    }
}