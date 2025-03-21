package com.business.company.infrastructure.repository;

import com.business.company.domain.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CompanyJpaRepository extends JpaRepository<Company, UUID> {
}