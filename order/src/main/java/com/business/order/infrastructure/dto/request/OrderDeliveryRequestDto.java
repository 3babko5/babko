package com.business.order.infrastructure.dto.request;

import java.util.UUID;

public class OrderDeliveryRequestDto {

    private UUID orderId;
    private Long userId;
    private UUID receiverId;
    private String deliveryAddress;
    private UUID originHubId;
    private UUID destinationHubId;

}
