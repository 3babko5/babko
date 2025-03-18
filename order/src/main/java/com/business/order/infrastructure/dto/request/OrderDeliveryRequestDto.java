package com.business.order.infrastructure.dto.request;

import java.util.UUID;

public class OrderDeliveryRequestDto {

    private UUID orderId;
    private UUID userId;
    private UUID receiverId;
    private String companyAddress;
    private UUID supplierHubId;
    private UUID receiverHubId;

}
