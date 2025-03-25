package com.business.order.application.dto.mapper;

import com.business.order.application.dto.response.*;
import com.business.order.domain.entity.Order;
import com.business.order.domain.entity.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

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

    public static OrderGetResponseDto toOrderGetResponse(Order order) {
        return OrderGetResponseDto.builder()
                .orderId(order.getOrderId())
                .receiverId(order.getReceiverId())
                .deliveryAddress(order.getDeliveryAddress())
                .totalPrice(order.getTotalPrice())
                .orderItems(toOrderItemResponseList(order.getOrderItems()))
                .build();
    }

    public static SearchOrderResponseDto toSearchOrderResponseDto(Page<Order> orderPage) {
        List<SearchOrderResponseDto.OrderData> orderDataList = orderPage.getContent().stream()
                .map(order -> SearchOrderResponseDto.OrderData.builder()
                        .orderId(order.getOrderId())
                        .userId(order.getUserId())
                        .receiverId(order.getReceiverId())
                        .deliveryAddress(order.getDeliveryAddress())
                        .totalPrice(order.getTotalPrice())
                        .orderStatus(order.getOrderStatus())
                        .build())
                .collect(Collectors.toList());
        SearchOrderResponseDto.PageInfo pageInfo = SearchOrderResponseDto.PageInfo.builder()
                .page(orderPage.getNumber())
                .size(orderPage.getSize())
                .totalElements(orderPage.getTotalElements())
                .totalPages(orderPage.getTotalPages())
                .isLast(orderPage.isLast())
                .sort(orderPage.getSort().toString())
                .direction(orderPage.getSort().stream().findFirst().map(Sort.Order::getDirection).orElse(Sort.Direction.DESC).name())
                .build();
        return SearchOrderResponseDto.builder()
                .message("조회 성공")
                .stateCode(200)
                .order(orderDataList)
                .pageInfo(pageInfo)
                .build();
    }

    public static OrderStatusResponseDto toOrderStatusResponseDto(Order order) {
        return OrderStatusResponseDto.builder()
                .orderId(order.getOrderId())
                .orderStatus(order.getOrderStatus())
                .build();
    }

}
