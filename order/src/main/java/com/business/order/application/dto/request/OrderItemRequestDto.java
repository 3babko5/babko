package com.business.order.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequestDto {
    private UUID productId;
    private Integer orderItemAmount;
    private Integer orderItemPrice;
}
