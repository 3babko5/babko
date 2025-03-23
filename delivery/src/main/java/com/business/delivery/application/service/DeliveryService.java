package com.business.delivery.application.service;

import com.business.common.application.exception.BusinessLogicException;
import com.business.common.infrastructure.api.NaverApiService;
import com.business.common.infrastructure.config.QueryDSLConfig;
import com.business.common.infrastructure.util.CommonUtil;
import com.business.common.infrastructure.util.JpaUtil;
import com.business.common.infrastructure.util.QueryDslUtil;
import com.business.delivery.DeliveryApplication;
import com.business.delivery.application.dto.mapper.DeliveryResponseMapper;
import com.business.delivery.application.dto.mapper.DeliveryRequestMapper;
import com.business.delivery.application.dto.request.CreateDeliveryRequestDto;
import com.business.delivery.application.dto.request.DeliverySearchRequestDto;
import com.business.delivery.application.dto.response.DeliveryDetailResponseDto;
import com.business.delivery.application.dto.response.DeliveryPageResponseDto;
import com.business.delivery.application.dto.response.DeliveryResponseDto;
import com.business.delivery.application.exception.DeliveryErrorCode;
import com.business.delivery.domain.entity.Delivery;
import com.business.delivery.domain.entity.DeliveryRoute;
import com.business.delivery.domain.repository.DeliveryRepository;
import com.business.delivery.infrastructure.client.HubClient;
import com.business.delivery.infrastructure.client.OrderClient;
import com.business.delivery.infrastructure.client.UserClient;
import com.business.delivery.infrastructure.dto.mapper.HubMapper;
import com.business.delivery.infrastructure.dto.response.HubIdResponseDto;
import com.business.delivery.infrastructure.dto.response.HubRoutesResponseDto;
import java.math.BigDecimal;
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

        Delivery savedDelivery = deliveryRepository.save(delivery);

        assignDeliveryDriver(savedDelivery.getDeliveryId());

        return DeliveryResponseMapper.deliveryToDeliveryResponseDto(savedDelivery);
    }

    private void assignDeliveryDriver(UUID deliveryId) {

        userClient.assignDeliveryDriver(deliveryId);
    }

    @Transactional(readOnly = true)
    public Page<DeliveryPageResponseDto> getDeliveries(DeliverySearchRequestDto request) {

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
}