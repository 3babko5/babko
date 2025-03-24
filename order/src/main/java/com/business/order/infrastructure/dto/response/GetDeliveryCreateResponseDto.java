package com.business.order.infrastructure.dto.response;

import com.business.order.domain.entity.DeliveryStatus;

import java.util.UUID;

public class GetDeliveryCreateResponseDto {
    private UUID deliveryId;
    private UUID orderId;
    private DeliveryStatus deliveryStatus;
}
