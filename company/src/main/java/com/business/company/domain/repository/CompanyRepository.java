package com.business.company.domain.repository;

import com.business.company.domain.entity.Company;
import com.business.company.domain.entity.CompanyType;

import java.util.List;

public interface CompanyRepository {
    Company save(Company company);
    List<Company> search(String companyName, CompanyType companyType);
}