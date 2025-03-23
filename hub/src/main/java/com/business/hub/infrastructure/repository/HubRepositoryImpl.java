package com.business.hub.infrastructure.repository;

import com.business.common.infrastructure.util.CommonUtil;
import com.business.common.infrastructure.util.QueryDslUtil;
import com.business.hub.application.dto.request.HubSearchRequest;
import com.business.hub.domain.entity.Hub;
import com.business.hub.domain.entity.QHub;
import com.business.hub.domain.repository.HubQueryDslRepository;
import com.business.hub.domain.repository.HubRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class HubRepositoryImpl implements HubRepository, HubQueryDslRepository {

    private final HubJpaRepository hubJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;
    QHub hub = QHub.hub;


    @Override
    public Hub save(Hub hub) {
        return hubJpaRepository.save(hub);
    }

    @Override
    public boolean existsByHubId(UUID hubId) {
        return hubJpaRepository.existsByHubId(hubId);
    }

    @Override
    public boolean existsByHubName(String hubName) {
        return hubJpaRepository.existsByHubName(hubName);
    }

    @Override
    public Optional<Hub> findById(UUID departureHubId) {
        return hubJpaRepository.findById(departureHubId);
    }

    @Override
    public List<Hub> findAll() {
        return hubJpaRepository.findAll();
    }

    @Override
    public boolean existsByHubNameAndHubAddress(String hubName, String hubAddress) {
        return hubJpaRepository.existsByHubNameAndHubAddress(hubName, hubAddress);
    }

    @Override
    public Optional<Hub> findByHubIdAndDeletedAtIsNullAndDeletedByIsNull(UUID hubId){
        return hubJpaRepository.findByHubIdAndDeletedAtIsNullAndDeletedByIsNull(hubId);
    }

    @Override
    public Page<Hub> findAllByDeletedAtIsNullAndDeletedByIsNull(Pageable pageable) {
        return hubJpaRepository.findAllByDeletedAtIsNullAndDeletedByIsNull(pageable);
    }

    @Override
    public Page<Hub> searchByRequest(HubSearchRequest request, Pageable pageable) {

        List<Hub> content = jpaQueryFactory
                .selectFrom(hub)
                .where(
                        containsHubName(request.getHubName()),
                        containsHubAddress(request.getHubAddress())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(QueryDslUtil.getAllOrderSpecifierArr(pageable, hub))
                .fetch();

        long total = jpaQueryFactory
                .select(hub.count())
                .from(hub)
                .where(
                        containsHubName(request.getHubName()),
                        containsHubAddress(request.getHubAddress())
                )
                .fetchOne();
        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression containsHubName(String hubName) {
        return CommonUtil.checkStringIsEmpty(hubName) ? null : hub.hubName.containsIgnoreCase(hubName);
    }

    private BooleanExpression containsHubAddress(String hubAddress) {
        return CommonUtil.checkStringIsEmpty(hubAddress) ? null : hub.hubAddress.containsIgnoreCase(hubAddress);
    }
}



