package com.business.order.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponseDto {
    private UUID itemId;
    private Integer orderItemAmount;
    private Long orderItemPrice;
    private UUID productId;
}
