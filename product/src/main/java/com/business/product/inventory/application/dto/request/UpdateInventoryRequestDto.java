package com.business.product.inventory.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class UpdateInventoryRequestDto {

    @NotBlank(message = "상품 ID는 필수입니다.")
    private UUID productId;

    @NotNull(message = "상품 개수는 필수입니다.")
    private Integer productQuantity;
}
