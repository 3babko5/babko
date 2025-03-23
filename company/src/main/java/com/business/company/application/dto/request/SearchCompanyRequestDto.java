package com.business.company.application.dto.request;

import com.business.company.domain.entity.CompanyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchCompanyRequestDto {

    private String companyName;
    private CompanyType companyType;
    private Integer page;
    private Integer size;
    private String orderBy;
    private String sort;
}