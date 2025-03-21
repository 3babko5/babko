package com.business.product.product.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class CreateProductResponseDto {
    private String message;
    private int stateCode;
    private ProductData product;

    @Getter
    @Builder
    public static class ProductData {
        private UUID productId;
        private String productName;
        private Integer productPrice;
        private Integer productQuantity;
        private UUID companyId;
    }
}
