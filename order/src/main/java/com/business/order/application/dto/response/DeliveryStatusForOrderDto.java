package com.business.order.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryStatusForOrderDto {
    private UUID deliveryId;
    private UUID orderId;
    private String deliveryStatus;
}
