package com.business.product.inventory.application.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Builder;

import java.util.UUID;

@Getter
@Builder
public class InventoryResponseDto {
    private String message;
    private int stateCode;
    private ProductData product;

    @Getter
    @Builder
    public static class ProductData {
        private UUID productId;
        private String productName;
        private String productQuantity;
        private UUID companyId;
    }
}
