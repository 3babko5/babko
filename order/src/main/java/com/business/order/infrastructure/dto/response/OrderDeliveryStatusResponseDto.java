package com.business.order.infrastructure.dto.response;

import java.util.UUID;

public class OrderDeliveryStatusResponseDto {
    //배송 상태 조회 응답 dto
    private UUID orderId;
    private UUID deliveryId;
    private String deliveryStatus;
}
