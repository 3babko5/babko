package com.business.order.application.dto.mapper;

import com.business.order.application.dto.response.OrderCreateResponseDto;
import com.business.order.application.dto.response.OrderItemResponseDto;
import com.business.order.domain.entity.Order;
import com.business.order.domain.entity.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderCreateResponseDto toOrderCreateResponseDto(Order order, List<OrderItem> orderItems) {
        return OrderCreateResponseDto.builder()
                .orderId(order.getOrderId())
                .receiverId(order.getReceiverId())
                .deliveryAddress(order.getDeliveryAddress())
                .totalPrice(order.getTotalPrice())
                .orderItems(toOrderItemResponseList(orderItems))
                .build();
    }

    private static List<OrderItemResponseDto> toOrderItemResponseList(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(OrderMapper::toOrderItemResponse)
                .collect(Collectors.toList());
    }

    private static OrderItemResponseDto toOrderItemResponse(OrderItem item) {
        return OrderItemResponseDto.builder()
                .orderItemId(item.getOrderItemId())
                .orderItemAmount(item.getOrderItemAmount())
                .orderItemPrice(item.getOrderItemPrice())
                .productId(item.getProductId())
                .supplierId(item.getSupplierId())
                .build();
    }
}
