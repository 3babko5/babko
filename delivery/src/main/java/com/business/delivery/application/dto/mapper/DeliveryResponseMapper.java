package com.business.delivery.application.dto.mapper;

import com.business.common.infrastructure.util.CommonUtil;
import com.business.delivery.application.dto.response.DeliveryPageResponseDto;
import com.business.delivery.application.dto.response.DeliveryResponseDto;
import com.business.delivery.domain.entity.Delivery;
import com.business.delivery.domain.entity.DeliveryRoute;
import java.util.stream.Collectors;

public class DeliveryResponseMapper {

  public static DeliveryResponseDto deliveryToDeliveryResponseDto(Delivery delivery) {

    return DeliveryResponseDto.builder()
        .deliveryId(delivery.getDeliveryId())
        .orderId(delivery.getOrderId())
        .deliveryStatus(delivery.getDeliveryStatus())
        .build();
  }

  public static DeliveryPageResponseDto deliveryToPageResponseDto(Delivery delivery) {

    return DeliveryPageResponseDto.builder()
        .deliveryId(delivery.getDeliveryId())
        .orderId(delivery.getOrderId())
        .deliveryStatus(delivery.getDeliveryStatus())
        .startHubId(delivery.getStartHubId())
        .endHubId(delivery.getEndHubId())
        .deliveryAddress(delivery.getDeliveryAddress())
        .recipientId(delivery.getRecipientId())
        .recipientSlackId(delivery.getRecipientSlackId())
        .createdAt(CommonUtil.LDTToString(delivery.getCreatedAt()))
        .updatedAt(delivery.getUpdatedAt() != null ? CommonUtil.LDTToString(delivery.getUpdatedAt()) : null)
        .deliveryRoutes(delivery.getDeliveryRoutes().stream()
            .map(DeliveryResponseMapper::deliveryToPageResponseDto)
            .collect(Collectors.toList())
        )
        .build();
  }

  private static DeliveryPageResponseDto.DeliveryRoutePageResponseDto deliveryToPageResponseDto(
      DeliveryRoute route) {

    return DeliveryPageResponseDto.DeliveryRoutePageResponseDto.builder()
        .deliveryRouteId(route.getDeliveryRouteId())
        .routeSequence(route.getRouteSequence())
        .originHubId(route.getOriginHubId())
        .destinationHubId(route.getDestinationHubId())
        .estimatedDistance(route.getEstimatedDistance())
        .estimatedTime(route.getEstimatedTime())
        .actualDistance(route.getActualDistance())
        .actualTime(route.getActualTime())
        .deliveryRouteStatus(route.getDeliveryRouteStatus())
        .createdAt(CommonUtil.LDTToString(route.getCreatedAt()))
        .updatedAt(route.getUpdatedAt() != null ? CommonUtil.LDTToString(route.getUpdatedAt()) : null)
        .build();
  }
}
