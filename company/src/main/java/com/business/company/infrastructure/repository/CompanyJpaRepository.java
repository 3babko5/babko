package com.business.company.infrastructure.repository;

import com.business.company.domain.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CompanyJpaRepository extends JpaRepository<Company, UUID> {
    @Query("SELECT c FROM Company c WHERE c.companyId = :companyId AND c.isDeleted = false")
    Optional<Company> findActiveCompanyById(@Param("companyId") UUID companyId);

}