package com.business.company.application.dto.request;

import com.business.company.domain.entity.CompanyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCompanyRequestDto {

    @NotBlank(message = "업체명은 필수입니다.")
    private String companyName;

    @NotBlank(message = "업체 주소는 필수입니다.")
    private String companyAddress;

    @NotNull(message = "업체 유형은 필수입니다.")
    private CompanyType companyType;

    @NotNull(message = "해당 업체의 Hub ID는 필수입니다.")
    private UUID hubId;

    @NotNull(message = "해당 업체의 관리자 ID는 필수입니다.")
    private UUID companyManagerId;
}