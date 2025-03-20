package com.business.company.application.service;

import com.business.company.application.dto.request.CreateCompanyRequestDto;
import com.business.company.application.dto.request.SearchCompanyRequestDto;
import com.business.company.application.dto.response.CreateCompanyResponseDto;
import com.business.company.application.dto.response.SearchCompanyResponseDto;
import com.business.company.application.mapper.CompanyMapper;
import com.business.company.domain.entity.Company;
import com.business.company.domain.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Transactional
    public CreateCompanyResponseDto createCompany(CreateCompanyRequestDto dto) {
        Company company = CompanyMapper.toEntity(dto);
        Company saved = companyRepository.save(company);
        return CompanyMapper.toResponseDto(saved);
    }

    @Transactional(readOnly = true)
    public SearchCompanyResponseDto searchCompanies(SearchCompanyRequestDto request) {
        final List<Company> companies = companyRepository.search(request.getCompanyName(), request.getCompanyType());

        return CompanyMapper.toSearchResponseDto(companies);
    }
}