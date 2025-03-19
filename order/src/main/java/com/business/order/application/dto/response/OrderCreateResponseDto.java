package com.business.order.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateResponseDto {

    private UUID orderId;
    private UUID receiverId;
    private String deliveryAddress;
    private Integer totalPrice;
    private List<OrderItemResponseDto> orderItems;
    private UUID supplierId;
    private UUID deliveryId;

}
