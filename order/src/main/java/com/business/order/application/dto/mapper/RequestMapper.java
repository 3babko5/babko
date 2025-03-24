package com.business.order.application.dto.mapper;

import com.business.order.application.dto.response.ProductDetailResponseDto;
import com.business.order.application.dto.response.hubIdResponseDto;
import com.business.order.domain.entity.CompanyType;
import com.business.order.infrastructure.dto.response.GetCompanyInfoResponseDto;
import com.business.order.infrastructure.dto.response.GetProductInfoResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public static hubIdResponseDto toHubIdResponse(GetCompanyInfoResponseDto response) {
        if (response == null || response.getCompany() == null) {
            return new hubIdResponseDto(Collections.emptyList());
        }

        List<hubIdResponseDto.CompanyData> companyList = response.getCompany().stream()
                .map(c -> new hubIdResponseDto.CompanyData(
                        c.getCompanyId(),
                        c.getCompanyType(),
                        c.getHubId()
                ))
                .collect(Collectors.toList());

        return new hubIdResponseDto(companyList);
    }



}
