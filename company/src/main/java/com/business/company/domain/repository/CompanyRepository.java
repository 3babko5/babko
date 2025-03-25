package com.business.company.domain.repository;

import com.business.company.domain.entity.Company;
import com.business.company.domain.entity.CompanyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository {
    Company save(Company company);
    Page<Company> search(String companyName, CompanyType companyType, UUID companyId, Pageable pageable);
    Optional<Company> findById(UUID companyId);
}