package com.business.company.application.dto.response;

import com.business.company.domain.entity.CompanyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class SearchCompanyResponseDto {
    private final String message;
    private final int stateCode;
    private final List<CompanyData> company;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CompanyData {
        private final UUID companyId;
        private final String companyName;
        private final CompanyType companyType;
        private final UUID hubId;
        private final UUID companyManagerId;
    }
}
