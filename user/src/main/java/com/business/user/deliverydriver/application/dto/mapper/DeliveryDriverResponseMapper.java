package com.business.user.deliverydriver.application.dto.mapper;

import com.business.user.deliverydriver.application.dto.response.AssignDeliveryDriverResponseDto;
import com.business.user.deliverydriver.application.dto.response.DeliveryDriverListResponseDto;
import com.business.user.deliverydriver.application.dto.response.DeliveryDriverResponseDto;
import com.business.user.deliverydriver.domain.entity.DeliveryDriver;
import org.springframework.data.domain.Page;

public class DeliveryDriverResponseMapper {

  public static DeliveryDriverResponseDto driverToDriverResponseDto(DeliveryDriver deliveryDriver) {
    return DeliveryDriverResponseDto.builder()
        .deliveryDriverId(deliveryDriver.getDeliveryDriverId())
        .hubId(deliveryDriver.getHubId())
        .slackId(deliveryDriver.getSlackId())
        .driverType(deliveryDriver.getDriverType())
        .deliverySequence(deliveryDriver.getDeliverySequence())
        .build();
  }

  public static AssignDeliveryDriverResponseDto driverToAssignedDto(DeliveryDriver deliveryDriver) {
    return AssignDeliveryDriverResponseDto.builder()
        .deliveryDriverId(deliveryDriver.getDeliveryDriverId())
        .driverType(deliveryDriver.getDriverType())
        .hubId(deliveryDriver.getHubId())
        .deliveryRouteId(deliveryDriver.getDeliveryRouteId())
        .routeSequence(deliveryDriver.getRouteSequence())
        .assignAt(deliveryDriver.getAssignAt())
        .build();
  }

  public static <T> DeliveryDriverListResponseDto<T> toPageListResponse(Page<T> page) {
    return DeliveryDriverListResponseDto.<T>builder()
        .totalElements(page.getTotalElements())
        .totalPages(page.getTotalPages())
        .currentPage(page.getNumber() + 1)
        .size(page.getSize())
        .data(page.getContent())
        .build();
  }
}