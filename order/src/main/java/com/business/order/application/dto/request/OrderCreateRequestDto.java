package com.business.order.application.dto.request;

import java.util.List;
import java.util.UUID;

public class OrderCreateRequestDto {

    private UUID supplierId;
    private UUID receiverId;
    private UUID supplierHubId;
    private UUID receiverHubId;
    private String companyAddress;
    private List<OrderItemRequestDto> items;

}
