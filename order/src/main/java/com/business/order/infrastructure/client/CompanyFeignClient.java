package com.business.order.infrastructure.client;

import com.business.order.domain.entity.CompanyType;
import com.business.order.infrastructure.dto.response.hubIdResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "company-service", url = "http://company-service/api/v1/companies")
public interface CompanyFeignClient {

    @GetMapping("/search")
    hubIdResponseDto searchCompanies(
            @RequestParam("companyType") CompanyType companyType
    );

}
