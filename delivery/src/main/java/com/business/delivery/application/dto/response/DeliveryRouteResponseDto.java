package com.business.delivery.application.dto.response;

import com.business.delivery.domain.entity.DeliveryRouteStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryRouteResponseDto {

  private UUID deliveryId;
  private UUID startHubId;
  private UUID endHubId;
  private List<RouteInfo> routes;

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class RouteInfo {

    private Long routeSequence;
    private UUID originHubId;
    private UUID destinationHubId;
    private BigDecimal estimatedDistance;
    private Long estimatedTime;
    private DeliveryRouteStatus deliveryRouteStatus;
    }
}