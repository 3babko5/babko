package com.business.order.application.dto.response;

import com.business.order.domain.entity.CompanyType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class hubIdResponseDto {
    private List<CompanyData> company;

    @Getter
    @NoArgsConstructor
    public static class CompanyData {
        private UUID companyId;
        private CompanyType companyType;
        private UUID hubId;
    }
}
