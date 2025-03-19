package com.business.user.deliverydriver.application.dto.mapper;

import com.business.user.deliverydriver.application.dto.response.AssignDeliveryDriverResponseDto;
import com.business.user.deliverydriver.application.dto.response.DeliveryDriverResponseDto;
import com.business.user.deliverydriver.domain.entity.DeliveryDriver;

public class DeliveryDriverMapper {

  public static DeliveryDriverResponseDto toDto(DeliveryDriver deliveryDriver) {
    return DeliveryDriverResponseDto.builder()
        .deliveryDriverId(deliveryDriver.getDeliveryDriverId())
        .hubId(deliveryDriver.getHubId())
        .slackId(deliveryDriver.getSlackId())
        .driverType(deliveryDriver.getDriverType())
        .deliverySequence(deliveryDriver.getDeliverySequence())
        .build();
  }

  public static AssignDeliveryDriverResponseDto toAssignedDto(DeliveryDriver deliveryDriver) {
    return AssignDeliveryDriverResponseDto.builder()
        .deliveryDriverId(deliveryDriver.getDeliveryDriverId())
        .driverType(deliveryDriver.getDriverType())
        .hubId(deliveryDriver.getHubId())
        .deliveryRouteId(deliveryDriver.getDeliveryRouteId())
        .routeSequence(deliveryDriver.getRouteSequence())
        .assignAt(deliveryDriver.getAssignAt())
        .build();
  }
}