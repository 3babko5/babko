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
    private Integer page;
    private Integer size;
    private String orderBy;
    private String sort;
}