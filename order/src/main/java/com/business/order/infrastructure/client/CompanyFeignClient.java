package com.business.order.infrastructure.client;

import com.business.order.domain.entity.CompanyType;
import com.business.order.infrastructure.dto.response.GetCompanyInfoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "company-service", url = "http://company-service/api/v1/companies")
public interface CompanyFeignClient {

    @GetMapping
    GetCompanyInfoResponseDto searchCompanies(
            @RequestParam(value = "companyName", required = false) String companyName,
            @RequestParam(value = "companyType", required = false) CompanyType companyType,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("orderBy") String orderBy,
            @RequestParam("sort") String sort
    );

}
