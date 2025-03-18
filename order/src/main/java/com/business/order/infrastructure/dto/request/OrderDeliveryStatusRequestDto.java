package com.business.order.infrastructure.dto.request;

import java.util.UUID;

public class OrderDeliveryStatusRequestDto {
    //배송상태 요청
    private UUID orderId;
    private UUID deliveryId;
}
