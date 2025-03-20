package com.business.delivery.infrastructure.dto.mapper;

import com.business.delivery.domain.entity.DeliveryRoute;
import com.business.delivery.domain.entity.DeliveryRouteStatus;
import com.business.delivery.infrastructure.dto.response.HubMovementResponseDto;
import java.util.UUID;

public class HubMovementMapper {

  public static DeliveryRoute toEntity(HubMovementResponseDto hubMovement, UUID deliveryId, Long routeSequence) {

    return DeliveryRoute.deliveryRouteCreateBuilder()
        .deliveryId(deliveryId)
        .routeSequence(routeSequence)
        .originHubId(hubMovement.getDepartureHubId())
        .destinationHubId(hubMovement.getArrivalHubId())
        .estimatedDistance(hubMovement.getDistance())
        .estimatedTime(hubMovement.getDurationTime())
        .deliveryRouteStatus(DeliveryRouteStatus.WAITING_AT_HUB)
        .build();
  }
}
