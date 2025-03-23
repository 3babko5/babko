package com.business.product.product.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchProductRequestDto {

    private String productName;
    private UUID companyId;
    private Integer page = 1;
    private Integer size = 10;
    private String orderBy = "CREATED";
    private String sort = "desc";
}