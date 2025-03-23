package com.business.user.deliverydriver.infrastructure.dto.response;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryClientResponseDto {

  private UUID deliveryRouteId;
  private UUID deliveryId;
  private UUID originHubId;
  private UUID destinationHubId;
  private Long routeSequence;
  private String deliveryAddress;
  private String deliveryRouteStatus;
  private BigDecimal estimatedDistance;
  private Long estimatedTime;
  private BigDecimal actualDistance;
  private Long actualTime;
}
