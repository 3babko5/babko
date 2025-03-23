package com.business.company.application.dto.response;

import com.business.company.domain.entity.CompanyType;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCompanyResponseDto {
    private String message;
    private int stateCode;
    private CompanyData company;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompanyData {
        private UUID companyId;
        private String companyName;
        private CompanyType companyType;
        private UUID hubId;
        private Long companyManagerId;
    }
}