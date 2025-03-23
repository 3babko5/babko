package com.business.product.product.application.mapper;

import com.business.product.product.application.dto.request.CreateProductRequestDto;
import com.business.product.product.application.dto.response.CreateProductResponseDto;
import com.business.product.product.application.dto.response.SearchProductResponseDto;
import com.business.product.product.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {

    public static Product toEntity(CreateProductRequestDto dto) {
        return Product.builder()
                .productName(dto.getProductName())
                .productPrice(dto.getProductPrice())
                .productQuantity(dto.getProductQuantity())
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
                        .productQuantity(product.getProductQuantity())
                        .companyId(product.getCompanyId())
                        .build())
                .build();
    }

    public static SearchProductResponseDto toSearchResponseDto(Page<Product> productPage) {
        List<SearchProductResponseDto.ProductData> companyDataList = productPage.getContent().stream()
                .map(product -> SearchProductResponseDto.ProductData.builder()
                        .productId(product.getProductId())
                        .productName(product.getProductName())
                        .productPrice(product.getProductPrice())
                        .companyId(product.getCompanyId())
                        .build())
                .collect(Collectors.toList());

        SearchProductResponseDto.PageInfo pageInfo = SearchProductResponseDto.PageInfo.builder()
                .page(productPage.getNumber())
                .size(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .isLast(productPage.isLast())
                .sort(productPage.getSort().toString())
                .direction(productPage.getSort().stream().findFirst().map(Sort.Order::getDirection).orElse(Sort.Direction.DESC).name())
                .build();

        return SearchProductResponseDto.builder()
                .message("조회 완료하였습니다.")
                .stateCode(200)
                .product(companyDataList)
                .pageInfo(pageInfo)
                .build();
    }
}