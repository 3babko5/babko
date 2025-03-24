package com.business.user.deliverydriver.infrastructure.repository;

import com.business.common.infrastructure.util.QueryDslUtil;
import com.business.user.deliverydriver.application.dto.request.DeliveryDriverSearchRequestDto;
import com.business.user.deliverydriver.domain.entity.DeliveryDriver;
import com.business.user.deliverydriver.domain.entity.DriverType;
import com.business.user.deliverydriver.domain.entity.QDeliveryDriver;
import com.business.user.deliverydriver.domain.repository.DeliveryDriverRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DeliveryDriverRepositoryImpl implements DeliveryDriverRepository {

    private final DeliveryDriverJpaRepository deliveryDriverJpaRepository;
    private final EntityManager entityManager;

    @Override
    public Optional<Long> findLastDeliverySequenceForHubDrivers() {
        return deliveryDriverJpaRepository.findLastDeliverySequenceForHubDrivers();
    }

    @Override
    public Optional<Long> findLastDeliverySequenceForCompanyDrivers(UUID hubId) {
        return deliveryDriverJpaRepository.findLastDeliverySequenceForCompanyDrivers(hubId);
    }

    @Override
    public long countByDriverType(DriverType driverType) {
        return deliveryDriverJpaRepository.countByDriverType(driverType);
    }

    @Override
    public long countByHubIdAndDriverType(UUID hubId, DriverType driverType) {
        return deliveryDriverJpaRepository.countByHubIdAndDriverType(hubId, driverType);
    }

    @Override
    public Optional<DeliveryDriver> findLastAssignedDriver() {
        return deliveryDriverJpaRepository.findFirstByAssignAtIsNotNullAndDeletedAtIsNullOrderByAssignAtDesc();
    }

    @Override
    public Optional<DeliveryDriver> findNextAvailableDriver(Long currentSequence) {
        return deliveryDriverJpaRepository.findFirstByDeliverySequenceGreaterThanAndDeletedAtIsNullOrderByDeliverySequenceAsc(currentSequence);
    }

    @Override
    public Optional<DeliveryDriver> findFirstAvailableDriver() {
        return deliveryDriverJpaRepository.findFirstByDeletedAtIsNullOrderByDeliverySequenceAsc();
    }

    @Override
    public Optional<DeliveryDriver> findLastAssignedDriverByTypeAndHub(DriverType driverType, UUID hubId) {
        return deliveryDriverJpaRepository.findFirstByAssignAtIsNotNullAndDeletedAtIsNullAndDriverTypeAndHubIdOrderByAssignAtDesc(driverType, hubId);
    }

    @Override
    public Optional<DeliveryDriver> findNextAvailableDriverByTypeAndHub(Long currentSequence, DriverType driverType, UUID hubId) {
        return deliveryDriverJpaRepository.findFirstByDeliverySequenceGreaterThanAndDeletedAtIsNullAndDriverTypeAndHubIdOrderByDeliverySequenceAsc(currentSequence, driverType, hubId);
    }

    @Override
    public boolean existsById(Long deliveryDriverId) {
        return deliveryDriverJpaRepository.existsByDeliveryDriverIdAndDeletedAtIsNull(deliveryDriverId);
    }

    @Override
    public Optional<DeliveryDriver> findByDeliveryDriverId(Long deliveryDriverId) {
        return deliveryDriverJpaRepository.findByDeliveryDriverIdAndDeletedAtIsNull(deliveryDriverId);
    }

    @Override
    public DeliveryDriver save(DeliveryDriver deliveryDriver) {
        return deliveryDriverJpaRepository.save(deliveryDriver);
    }

    public Page<DeliveryDriver> findDeliveryDrivers(DeliveryDriverSearchRequestDto request, Pageable pageable) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        QDeliveryDriver driver = QDeliveryDriver.deliveryDriver;

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(driver.deletedAt.isNull());

        if (request.getDeliveryDriverId() != null) {
            builder.and(driver.deliveryDriverId.eq(request.getDeliveryDriverId()));
        }

        if (request.getHubId() != null) {
            builder.and(driver.hubId.eq(request.getHubId()));
        }

        if (request.getSlackId() != null) {
            builder.and(driver.slackId.eq(request.getSlackId()));
        }

        if (request.getDriverType() != null) {
            builder.and(driver.driverType.eq(request.getDriverType()));
        }

        if (request.getDeliverySequence() != null) {
            builder.and(driver.deliverySequence.eq(request.getDeliverySequence()));
        }

        List<DeliveryDriver> content = queryFactory
            .selectFrom(driver)
            .where(builder)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(QueryDslUtil.getAllOrderSpecifierArr(pageable, driver))
            .fetch();

        Long total = queryFactory
            .select(driver.count())
            .from(driver)
            .where(builder)
            .fetchOne();

        return PageableExecutionUtils.getPage(content, pageable, () -> total == null ? 0 : total);
    }

    @Override
    public Optional<DeliveryDriver> findByDeliveryRouteId(UUID deliveryRouteId) {
        return deliveryDriverJpaRepository.findByDeliveryRouteIdAndDeletedAtIsNull(deliveryRouteId);
    }
}
