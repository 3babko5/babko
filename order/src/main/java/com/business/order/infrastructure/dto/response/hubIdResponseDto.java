package com.business.order.infrastructure.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
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
        private String companyType;
        private UUID hubId;
    }
}
