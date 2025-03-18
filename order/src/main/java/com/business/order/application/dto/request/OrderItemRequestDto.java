package com.business.order.application.dto.request;

import java.util.UUID;

public class OrderItemRequestDto {
    private UUID productId;
    private Integer orderItemAmount;
    private Integer orderItemPrice;
}
