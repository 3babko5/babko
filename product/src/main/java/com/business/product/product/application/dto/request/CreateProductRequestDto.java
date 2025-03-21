package com.business.product.product.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class CreateProductRequestDto {

    @NotBlank(message = "상품명은 필수입니다.")
    private String productName;

    @NotNull(message = "상품 가격은 필수입니다.")
    private Integer productPrice;

    @NotNull(message = "상품 수량은 필수입니다.")
    private Integer productQuantity;

    @NotNull(message = "해당 상품을 갖고 있는 업체 ID는 필수입니다.")
    private UUID companyId;
}
