package com.business.product.product.application.mapper;

import com.business.product.product.application.dto.request.CreateProductRequestDto;
import com.business.product.product.application.dto.response.CreateProductResponseDto;
import com.business.product.product.domain.entity.Product;

public class ProductMapper {

    public static Product toEntity(CreateProductRequestDto dto) {
        return Product.builder()
                .productName(dto.getProductName())
                .productPrice(dto.getProductPrice())
                .companyId(dto.getCompanyId())
                .build();
    }

    public static CreateProductResponseDto toResponseDto(Product product) {
        return CreateProductResponseDto.builder()
                .message("상품이 등록되었습니다.")
                .stateCode(201)
                .product(CreateProductResponseDto.ProductData.builder()
                        .productId(product.getProductId())
                        .productName(product.getProductName())
                        .productPrice(product.getProductPrice())
                        .companyId(product.getCompanyId())
                        .build())
                .build();
    }
}
