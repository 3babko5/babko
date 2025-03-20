package com.business.company.infrastructure.repository;

import com.business.company.domain.entity.Company;
import com.business.company.domain.entity.CompanyType;
import com.business.company.domain.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CompanyRepositoryImpl implements CompanyRepository {

    private final CompanyJpaRepository companyJpaRepository;

    @Override
    public Company save(Company company) {
        return companyJpaRepository.save(company);
    }

    @Override
    public List<Company> search(String companyName, CompanyType companyType) {
        if (companyName != null && companyType != null) {
            return companyJpaRepository.findByCompanyNameContainingAndCompanyType(companyName, companyType);
        } else if (companyName != null) {
            return companyJpaRepository.findByCompanyNameContaining(companyName);
        } else if (companyType != null) {
            return companyJpaRepository.findByCompanyType(companyType);
        } else {
            return companyJpaRepository.findAll();
        }
    }
}