package com.business.company.application.dto.request;

import com.business.company.domain.entity.CompanyType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchCompanyRequestDto {

    private final String companyName;
    private final CompanyType companyType;
    private final Integer page = 1;
    private final Integer size = 10;
    private final String orderBy = "CREATED";
    private final String sort = "desc";
}