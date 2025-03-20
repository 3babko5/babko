package com.business.order.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderGetResponseDto {
    private UUID orderId;
    private UUID receiverId;
    private String deliveryAddress;
    private Integer totalPrice;
    private List<OrderItemResponseDto> orderItems;
}
