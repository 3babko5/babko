package com.business.company.application.dto.request;

import com.business.company.domain.entity.CompanyType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchCompanyRequestDto {

    private final String companyName;
    private final CompanyType companyType;
    private final Integer page;
    private final Integer size;
    private final String orderBy;
    private final String sort;
}