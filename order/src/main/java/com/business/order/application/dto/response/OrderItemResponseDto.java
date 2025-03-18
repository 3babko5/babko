package com.business.order.application.dto.response;

import java.util.UUID;

public class OrderItemResponseDto {
    private UUID itemId;
    private UUID productId;
    private Integer orderItemAmount;
    private Integer orderItemPrice;
}
