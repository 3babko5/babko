package com.business.company.application.dto.response;

import com.business.company.domain.entity.Company;
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
    private UUID companyId;
    private String companyName;
    private CompanyType companyType;
    private UUID hubId;
    private UUID companyManagerId;

    public static CreateCompanyResponseDto from(Company company) {
        return CreateCompanyResponseDto.builder()
                .message("업체가 등록되었습니다.")
                .stateCode(201)
                .companyId(company.getCompanyId())
                .companyName(company.getCompanyName())
                .companyType(company.getCompanyType())
                .hubId(company.getHubId())
                .companyManagerId(company.getCompanyManagerId())
                .build();
    }
}
