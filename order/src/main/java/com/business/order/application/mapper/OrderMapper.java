package com.business.order.application.mapper;

import com.business.order.application.dto.response.OrderCreateResponseDto;
import com.business.order.application.dto.response.OrderItemResponseDto;
import com.business.order.domain.entity.Order;
import com.business.order.domain.entity.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    //주문 엔티티 > 주문 생성 응답 DTO 변환
    public static OrderCreateResponseDto toOrderCreateResponseDto(Order order, List<OrderItem> orderItems) {
        return OrderCreateResponseDto.builder()
                .orderId(order.getOrderId())
                .receiverId(order.getReceiverId())
                .deliveryAddress(order.getDeliveryAddress())
                .totalPrice(order.getTotalPrice())
                .orderItems(toOrderItemResponseList(orderItems))
                .build();
    }

    //주문 아이템 리스트 변환
    private static List<OrderItemResponseDto> toOrderItemResponseList(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(OrderMapper::toOrderItemResponse)
                .collect(Collectors.toList());
    }

    private static OrderItemResponseDto toOrderItemResponse(OrderItem item) {
        return OrderItemResponseDto.builder()
                .itemId(item.getOrderItemId())
                .orderItemAmount(item.getOrderItemAmount())
                .orderItemPrice(item.getOrderItemPrice())
                .build();
    }
}
