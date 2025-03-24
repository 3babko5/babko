package com.business.order.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailResponseDto {
    private UUID productId;

    @JsonProperty("productPrice")
    private Long orderItemPrice;

    private Integer productQuantity; //재고

    @JsonProperty("companyId")
    private UUID supplierId;
}
