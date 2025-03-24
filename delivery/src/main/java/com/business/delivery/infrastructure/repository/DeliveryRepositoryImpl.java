package com.business.delivery.infrastructure.repository;

import com.business.common.application.exception.BusinessLogicException;
import com.business.common.infrastructure.util.QueryDslUtil;
import com.business.delivery.application.dto.request.DeliverySearchRequestDto;
import com.business.delivery.application.exception.DeliveryErrorCode;
import com.business.delivery.domain.entity.Delivery;
import com.business.delivery.domain.entity.QDelivery;
import com.business.delivery.domain.repository.DeliveryRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DeliveryRepositoryImpl implements DeliveryRepository {

  private final DeliveryJpaRepository deliveryJpaRepository;
  private final EntityManager entityManager;

  @Override
  public Delivery save(Delivery delivery) {
    return deliveryJpaRepository.save(delivery);
  }

  @Override
  public Page<Delivery> findDeliveries(DeliverySearchRequestDto request, Pageable pageable) {

    JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

    QDelivery delivery = QDelivery.delivery;

    BooleanBuilder builder = new BooleanBuilder();

    builder.and(delivery.deletedAt.isNull());

    if (request.getOrderId() != null) {
      builder.and(delivery.orderId.eq(request.getOrderId()));
    }
    if (request.getStartHubId() != null) {
      builder.and(delivery.startHubId.eq(request.getStartHubId()));
    }
    if (request.getEndHubId() != null) {
      builder.and(delivery.endHubId.eq(request.getEndHubId()));
    }
    if (request.getDeliveryStatus() != null) {
      builder.and(delivery.deliveryStatus.eq(request.getDeliveryStatus()));
    }
    if (request.getOriginHubId() != null) {
      builder.and(
          delivery.deliveryRoutes.any().originHubId.eq(request.getOriginHubId())
      );
    }
    if (request.getDestinationHubId() != null) {
      builder.and(
          delivery.deliveryRoutes.any().destinationHubId.eq(request.getDestinationHubId())
      );
    }
    if (request.getDeliveryRouteStatus() != null) {

      builder.and(
          delivery
              .deliveryRoutes
              .any()
              .deliveryRouteStatus
              .eq(request.getDeliveryRouteStatus()));
    }

    OrderSpecifier<?>[] orderSpecifiers = QueryDslUtil.getAllOrderSpecifierArr(pageable, delivery);

    List<Delivery> deliveries =
        queryFactory
            .selectFrom(delivery)
            .leftJoin(delivery.deliveryRoutes)
            .fetchJoin()
            .where(builder)
            .orderBy(orderSpecifiers)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

    Long total = queryFactory
        .select(delivery.count())
        .from(delivery)
        .where(builder)
        .fetchOne();

    return PageableExecutionUtils.getPage(deliveries, pageable, () -> total == null ? 0 : total);
    }

  @Override
  public Delivery findByDeliveryId(UUID deliveryId) {

    return deliveryJpaRepository.findByDeliveryIdAndDeletedAtIsNull(deliveryId)
        .orElseThrow(() -> new BusinessLogicException(DeliveryErrorCode.DELIVERY_NOT_FOUND));
  }

  @Override
  public void deleteByDeliveryId(UUID deliveryId, Long deletedBy) {

    Delivery delivery = deliveryJpaRepository.findByDeliveryIdAndDeletedAtIsNull(deliveryId)
        .orElseThrow(() -> new BusinessLogicException(DeliveryErrorCode.DELIVERY_ALREADY_DELETED));

    delivery.softDelete(deletedBy);
  }
}