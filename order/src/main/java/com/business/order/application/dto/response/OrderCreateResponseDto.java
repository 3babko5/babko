package com.business.order.application.dto.response;

import java.util.List;
import java.util.UUID;

public class OrderCreateResponseDto {

    private UUID orderId;
    private UUID userId;
    private UUID supplierId;
    private UUID receiverId;
    private UUID deliveryId;
    private Integer totalPrice;
    private List<OrderItemResponseDto> orderItems;

}
