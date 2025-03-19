package com.business.company.application.service;

import com.business.company.application.dto.request.CreateCompanyRequestDto;
import com.business.company.application.dto.response.CreateCompanyResponseDto;
import com.business.company.domain.entity.Company;
import com.business.company.domain.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Transactional
    public CreateCompanyResponseDto createCompany(CreateCompanyRequestDto createCompanyRequestDto) {
        Company company = companyRepository.save(createCompanyRequestDto.toEntity());
        return CreateCompanyResponseDto.from(company);
    }
}
