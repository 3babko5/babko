package com.business.product.product.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class SearchProductRequestDto {

    private final String productName;
    private final UUID companyId;
    private final Integer page;
    private final Integer size;
    private final String orderBy;
    private final String sort;
}