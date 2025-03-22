package com.business.order.application.dto.response;

import com.business.order.domain.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusResponseDto {
    private UUID orderId;
    private OrderStatus orderStatus;
}
