package com.business.order.infrastructure.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailResponseDto {
    private UUID productId;
    private Long productPrice;
    private Integer productQuantity; //재고
    private UUID supplierId;
}
