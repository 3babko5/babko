package com.business.company.infrastructure.repository;

import com.business.common.infrastructure.util.QueryDslUtil;
import com.business.company.domain.entity.Company;
import com.business.company.domain.entity.CompanyType;
import static com.business.company.domain.entity.QCompany.company;
import com.business.company.domain.repository.CompanyRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CompanyRepositoryImpl implements CompanyRepository {

    private final CompanyJpaRepository companyJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Company save(Company company) {
        return companyJpaRepository.save(company);
    }

    @Override
    public Page<Company> search(String companyName, CompanyType companyType, UUID companyId, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        //  삭제한 업체 제외 조회하는 조건
        builder.and(company.isDeleted.eq(false));

        if (companyName != null && !companyName.isBlank()) {
            builder.and(company.companyName.containsIgnoreCase(companyName));
        }

        if (companyType != null) {
            builder.and(company.companyType.eq(companyType));
        }

        if (companyId != null) {
            builder.and(company.companyId.eq(companyId));
        }

        List<Company> results = queryFactory
                .selectFrom(company)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(QueryDslUtil.getAllOrderSpecifierArr(pageable, company))
                .fetch();

        long total = queryFactory
                .selectFrom(company)
                .where(builder)
                .fetchCount();

        return PageableExecutionUtils.getPage(results, pageable, () -> total);
    }

    @Override
    public Optional<Company> findActiveCompanyById(UUID companyId) {
        return companyJpaRepository.findActiveCompanyById(companyId);
    }
}