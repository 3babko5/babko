package com.business.user.infrastructure.dto.response;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeliveryRouteClientResponseDto {

  private UUID deliveryRouteId;
  private UUID deliveryId;
  private UUID originHubId;
  private UUID destinationHubId;
  private Long routeSequence;
  private String deliveryAddress;

  public DeliveryRouteClientResponseDto(
      UUID deliveryRouteId,
      UUID deliveryId,
      UUID originHubId,
      UUID destinationHubId,
      Long routeSequence,
      String deliveryAddress) {

    this.deliveryRouteId = deliveryRouteId;
    this.deliveryId = deliveryId;
    this.originHubId = originHubId;
    this.destinationHubId = destinationHubId;
    this.routeSequence = routeSequence;
    this.deliveryAddress = deliveryAddress;
  }
}
