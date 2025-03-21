package com.business.delivery.application.dto.mapper;

import com.business.delivery.application.dto.request.CreateDeliveryRequestDto;
import com.business.delivery.application.dto.response.DeliveryResponseDto;
import com.business.delivery.domain.entity.Delivery;
import com.business.delivery.domain.entity.DeliveryRoute;
import com.business.delivery.domain.entity.DeliveryRouteStatus;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DeliveryMapper {

  public static Delivery deliveryToEntity(CreateDeliveryRequestDto request) {

    return Delivery.deliveryCreateBuilder()
        .orderId(request.getOrderId())
        .startHubId(request.getStartHubId())
        .endHubId(request.getEndHubId())
        .deliveryAddress(request.getDeliveryAddress())
        .recipientId(request.getRecipientId())
        .build();
  }

  public static DeliveryResponseDto deliveryToDeliveryResponseDto(Delivery delivery) {

    return DeliveryResponseDto.builder()
        .deliveryId(delivery.getDeliveryId())
        .orderId(delivery.getOrderId())
        .deliveryStatus(delivery.getDeliveryStatus())
        .build();
  }

  public static DeliveryRoute toEntityForFinalRoute(
      UUID endHubId,
      BigDecimal finalEstimatedDistance,
      Long finalEstimatedTime,
      int currentRouteCount,
      String deliveryAddress
  ) {
    return DeliveryRoute.deliveryRouteCreateBuilder()
        .routeSequence((long) (currentRouteCount + 1))
        .originHubId(endHubId)
        .destinationHubId(null)
        .estimatedDistance(finalEstimatedDistance)
        .estimatedTime(finalEstimatedTime)
        .deliveryRouteStatus(DeliveryRouteStatus.PENDING)
        .deliveryAddress(deliveryAddress)
        .build();
  }

  public static List<DeliveryRoute> combineRoutes(List<DeliveryRoute> hubRoutes, DeliveryRoute finalRoute) {

    List<DeliveryRoute> allRoutes = new ArrayList<>(hubRoutes);
    allRoutes.add(finalRoute);
    return allRoutes;
  }
}
