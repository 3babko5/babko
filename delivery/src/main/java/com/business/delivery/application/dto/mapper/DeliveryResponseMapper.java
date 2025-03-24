package com.business.delivery.application.dto.mapper;

import com.business.common.infrastructure.util.CommonUtil;
import com.business.delivery.application.dto.response.DeliveryDetailResponseDto;
import com.business.delivery.application.dto.response.DeliveryDetailResponseDto.DeliveryRouteDetailResponseDto;
import com.business.delivery.application.dto.response.DeliveryPageListResponseDto;
import com.business.delivery.application.dto.response.DeliveryPageResponseDto;
import com.business.delivery.application.dto.response.DeliveryResponseDto;
import com.business.delivery.application.dto.response.DeliveryStatusUpdateResponseDto;
import com.business.delivery.domain.entity.Delivery;
import com.business.delivery.domain.entity.DeliveryRoute;
import com.business.delivery.infrastructure.dto.response.OrderInfoResponseDto;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

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

  public static DeliveryDetailResponseDto deliveryAndOrderToDetailResponse(
      Delivery delivery,
      Map<String, Object> orderResponse
  ) {
    List<Map<String, Object>> rawOrderItems =
        (List<Map<String, Object>>) orderResponse.get("orderItems");

    List<OrderInfoResponseDto> orderItems = rawOrderItems.stream()
        .map(item -> OrderInfoResponseDto.builder()
            .orderItemId(UUID.fromString(item.get("orderItemId").toString()))
            .orderItemAmount((Integer) item.get("orderItemAmount"))
            .orderItemPrice(Long.valueOf(item.get("orderItemPrice").toString()))
            .build())
        .collect(Collectors.toList());

    List<DeliveryRouteDetailResponseDto> routeDetail = delivery.getDeliveryRoutes().stream()
        .map(route -> DeliveryRouteDetailResponseDto.builder()
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
            .build())
        .collect(Collectors.toList());

    return DeliveryDetailResponseDto.builder()
        .deliveryId(delivery.getDeliveryId())
        .orderId(delivery.getOrderId())
        .totalPrice((Integer) orderResponse.get("totalPrice"))
        .deliveryStatus(delivery.getDeliveryStatus())
        .startHubId(delivery.getStartHubId())
        .endHubId(delivery.getEndHubId())
        .deliveryAddress(delivery.getDeliveryAddress())
        .recipientId(delivery.getRecipientId())
        .recipientSlackId(delivery.getRecipientSlackId())
        .createdAt(CommonUtil.LDTToString(delivery.getCreatedAt()))
        .updatedAt(delivery.getUpdatedAt() != null ? CommonUtil.LDTToString(delivery.getUpdatedAt()) : null)
        .deliveryRoutes(routeDetail)
        .orderItems(orderItems)
        .build();
  }

  public static <T> DeliveryPageListResponseDto<T> toPageListResponse(Page<T> page) {

    return DeliveryPageListResponseDto.<T>builder()
        .totalElements(page.getTotalElements())
        .totalPages(page.getTotalPages())
        .currentPage(page.getNumber() + 1)
        .size(page.getSize())
        .data(page.getContent())
        .build();
  }

  public static DeliveryStatusUpdateResponseDto toStatusUpdateResponse(Delivery delivery, DeliveryRoute deliveryRoute) {

    return DeliveryStatusUpdateResponseDto.builder()
        .deliveryStatus(delivery.getDeliveryStatus())
        .deliveryRouteStatus(deliveryRoute.getDeliveryRouteStatus())
        .build();
  }
}
