package com.business.order.application.dto.mapper;

import com.business.order.application.dto.response.ProductDetailResponseDto;
import com.business.order.infrastructure.dto.response.GetProductInfoResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestMapper {

    public static ProductDetailResponseDto toProductDetailResponse(GetProductInfoResponseDto.ProductData data) {
        if (data == null) {
            return null;
        }

        return new ProductDetailResponseDto(
                data.getProductId(),
                data.getProductPrice() != null ? data.getProductPrice().longValue() : null,
                data.getProductQuantity(),
                data.getCompanyId()
        );
    }



}
