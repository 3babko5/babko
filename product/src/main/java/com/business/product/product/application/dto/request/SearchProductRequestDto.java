package com.business.product.product.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class SearchProductRequestDto {

    private final String productName;
    private final UUID companyId;
    private final Integer page = 1;
    private final Integer size = 10;
    private final String orderBy = "CREATED";
    private final String sort = "desc";
}