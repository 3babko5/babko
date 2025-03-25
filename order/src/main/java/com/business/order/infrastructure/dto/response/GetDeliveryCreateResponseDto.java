package com.business.order.infrastructure.dto.response;

import com.business.order.domain.entity.DeliveryStatus;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
public class GetDeliveryCreateResponseDto {
    private UUID deliveryId;
    private UUID orderId;
    private DeliveryStatus deliveryStatus;
}
