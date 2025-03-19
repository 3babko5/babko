package com.business.company.domain.repository;

import com.business.company.domain.entity.Company;

public interface CompanyRepository {
    Company save(Company company);
}