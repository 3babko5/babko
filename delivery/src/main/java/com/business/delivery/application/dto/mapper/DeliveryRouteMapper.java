package com.business.delivery.application.dto.mapper;

import com.business.common.application.exception.BusinessLogicException;
import com.business.delivery.application.dto.response.DeliveryRouteResponseDto;
import com.business.delivery.application.exception.DeliveryRouteErrorCode;
import com.business.delivery.domain.entity.DeliveryRoute;
import java.util.List;
import java.util.stream.Collectors;

public class DeliveryRouteMapper {

  public static DeliveryRouteResponseDto.RouteInfo toRouteInfo(DeliveryRoute route) {

    return DeliveryRouteResponseDto.RouteInfo.builder()
        .routeSequence(route.getRouteSequence())
        .originHubId(route.getOriginHubId())
        .destinationHubId(route.getDestinationHubId())
        .estimatedDistance(route.getEstimatedDistance())
        .estimatedTime(route.getEstimatedTime())
        .deliveryRouteStatus(route.getDeliveryRouteStatus())
        .build();
  }

  public static DeliveryRouteResponseDto toDto(List<DeliveryRoute> deliveryRoutes) {

    if (deliveryRoutes == null || deliveryRoutes.isEmpty()) {
      throw new BusinessLogicException(DeliveryRouteErrorCode.ROUTE_NOT_FOUND);
    }

    return DeliveryRouteResponseDto.builder()
        .deliveryId(deliveryRoutes.get(0).getDeliveryId())
        .startHubId(deliveryRoutes.get(0).getOriginHubId())
        .endHubId(deliveryRoutes.get(deliveryRoutes.size() - 1).getDestinationHubId())
        .routes(deliveryRoutes.stream()
            .map(DeliveryRouteMapper::toRouteInfo)
            .collect(Collectors.toList()))
        .build();
  }
}