package com.business.user.deliverydriver.application.dto.mapper;

import com.business.common.infrastructure.util.CommonUtil;
import com.business.user.deliverydriver.application.dto.response.AssignDeliveryDriverResponseDto;
import com.business.user.deliverydriver.application.dto.response.DeliveryDriverDetailResponseDto;
import com.business.user.deliverydriver.application.dto.response.DeliveryDriverListResponseDto;
import com.business.user.deliverydriver.application.dto.response.DeliveryDriverResponseDto;
import com.business.user.deliverydriver.application.dto.response.DeliveryDriverRouteResponseDto;
import com.business.user.deliverydriver.domain.entity.DeliveryDriver;
import com.business.user.deliverydriver.infrastructure.dto.response.DeliveryClientResponseDto;
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

    public static DeliveryDriverDetailResponseDto driverToDriverDetailDto(
        DeliveryDriver deliveryDriver,
        DeliveryClientResponseDto deliveryRoute
    ) {
        return DeliveryDriverDetailResponseDto.builder()

            .deliveryDriverId(deliveryDriver.getDeliveryDriverId())
            .hubId(deliveryDriver.getHubId())
            .slackId(deliveryDriver.getSlackId())
            .driverType(deliveryDriver.getDriverType())
            .deliverySequence(deliveryDriver.getDeliverySequence())
            .assignAt(deliveryDriver.getAssignAt())
            .createdAt(CommonUtil.LDTToString(deliveryDriver.getCreatedAt()))
            .updatedAt(deliveryDriver.getUpdatedAt() != null ? CommonUtil.LDTToString(deliveryDriver.getUpdatedAt()) : null)
            .deliveryRoute(deliveryClientTodriverRouteDto(deliveryRoute))
            .build();
    }

    public static DeliveryDriverRouteResponseDto deliveryClientTodriverRouteDto(DeliveryClientResponseDto deliveryRoute) {
        return DeliveryDriverRouteResponseDto.builder()
            .originHubId(deliveryRoute.getOriginHubId())
            .destinationHubId(deliveryRoute.getDestinationHubId())
            .routeSequence(deliveryRoute.getRouteSequence())
            .deliveryAddress(deliveryRoute.getDeliveryAddress())
            .deliveryRouteStatus(deliveryRoute.getDeliveryRouteStatus())
            .estimatedDistance(deliveryRoute.getEstimatedDistance())
            .estimatedTime(deliveryRoute.getEstimatedTime())
            .actualDistance(deliveryRoute.getActualDistance())
            .actualTime(deliveryRoute.getActualTime())
            .build();
    }
}