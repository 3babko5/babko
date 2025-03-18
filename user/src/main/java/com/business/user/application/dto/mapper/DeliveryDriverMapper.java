package com.business.user.application.dto.mapper;

import com.business.user.application.dto.response.DeliveryDriverResponseDto;
import com.business.user.domain.entity.DeliveryDriver;

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
}