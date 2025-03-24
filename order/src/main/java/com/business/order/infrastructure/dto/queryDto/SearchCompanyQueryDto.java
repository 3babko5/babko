package com.business.order.infrastructure.dto.queryDto;

import com.business.order.domain.entity.CompanyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchCompanyQueryDto {
    private UUID companyId;
    private String companyName;
    private CompanyType companyType;
    private Integer page = 1;
    private Integer size = 10;
    private String orderBy = "CREATED";
    private String sort = "desc";
}
