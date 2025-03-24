package com.business.order.infrastructure.client;

import com.business.order.domain.entity.CompanyType;
import com.business.order.infrastructure.dto.queryDto.SearchCompanyQueryDto;
import com.business.order.infrastructure.dto.response.GetCompanyInfoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "company-service", url = "http://localhost:8087")
public interface CompanyFeignClient {

    @GetMapping("/api/v1/companies")
    GetCompanyInfoResponseDto searchCompanies(
            @SpringQueryMap SearchCompanyQueryDto request
    );

}
