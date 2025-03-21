package com.business.delivery.infrastructure.dto.mapper;

import com.business.delivery.domain.entity.DeliveryRoute;
import com.business.delivery.domain.entity.DeliveryRouteStatus;
import com.business.delivery.infrastructure.dto.request.HubRoutesRequestDto;
import com.business.delivery.infrastructure.dto.response.HubRoutesResponseDto;

public class HubMapper {

  public static HubRoutesRequestDto toDto(HubRoutesRequestDto request) {
    return HubRoutesRequestDto.builder()
      .departureHubId(request.getDepartureHubId())
        .arrivalHubId(request.getArrivalHubId())
        .build();
  }

  public static DeliveryRoute toEntityFromHubMovement(HubRoutesResponseDto hubMovement, Long routeSequence) {
    return DeliveryRoute.deliveryRouteCreateBuilder()
        .routeSequence(routeSequence)
        .originHubId(hubMovement.getDepartureHubId())
        .destinationHubId(hubMovement.getArrivalHubId())
        .estimatedDistance(hubMovement.getDistance())
        .estimatedTime(hubMovement.getDurationTime())
        .deliveryRouteStatus(DeliveryRouteStatus.PENDING)
        .deliveryAddress(null)
        .build();
  }
}
