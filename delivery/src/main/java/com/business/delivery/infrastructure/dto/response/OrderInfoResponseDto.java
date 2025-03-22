package com.business.delivery.infrastructure.dto.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfoResponseDto {

    private UUID orderItemId;
    private Integer orderItemAmount;
    private Long orderItemPrice;
    private Integer totalPrice;
}
