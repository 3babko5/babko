package com.business.company.infrastructure.repository;

import com.business.company.domain.entity.Company;
import com.business.company.domain.entity.CompanyType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CompanyJpaRepository extends JpaRepository<Company, UUID> {
    List<Company> findByCompanyNameContaining(String companyName);
    List<Company> findByCompanyType(CompanyType companyType);
    List<Company> findByCompanyNameContainingAndCompanyType(String companyName, CompanyType companyType);
}