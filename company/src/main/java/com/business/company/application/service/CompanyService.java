package com.business.company.application.service;

import com.business.company.application.dto.request.CreateCompanyRequestDto;
import com.business.company.application.dto.request.SearchCompanyRequestDto;
import com.business.company.application.dto.response.CreateCompanyResponseDto;
import com.business.company.application.dto.response.SearchCompanyResponseDto;
import com.business.company.application.mapper.CompanyMapper;
import com.business.company.domain.entity.Company;
import com.business.company.domain.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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
    public SearchCompanyResponseDto searchCompanies(SearchCompanyRequestDto request, Pageable pageable) {
        Page<Company> companyPage = companyRepository.search(
                request.getCompanyName(),
                request.getCompanyType(),
                request.getCompanyId(),
                pageable
        );
        return CompanyMapper.toSearchResponseDto(companyPage);
    }

    @Transactional
    public void deleteCompany(UUID companyId, Long userId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("해당 업체가 존재하지 않습니다."));

        company.deletedBy(userId);
    }
}