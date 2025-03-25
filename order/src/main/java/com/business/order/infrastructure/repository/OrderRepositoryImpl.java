package com.business.order.infrastructure.repository;

import com.business.common.infrastructure.util.QueryDslUtil;
import com.business.order.domain.entity.Order;
import static com.business.order.domain.entity.QOrder.order;
import com.business.order.domain.entity.OrderStatus;
import com.business.order.domain.repository.OrderRepository;
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
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Order save(Order order) {
        return orderJpaRepository.save(order);
    }

    @Override
    public Order findByOrderIdWithItems(UUID orderId) {
        return orderJpaRepository.findByOrderIdWithItems(orderId);
    }

    @Override
    public Optional<Order> findById(UUID orderId) {
        return orderJpaRepository.findById(orderId);
    }

    @Override
    public Page<Order> search(UUID orderId, OrderStatus orderStatus, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if(orderId != null) {
            builder.and(order.orderId.eq(orderId));
        }
        if(orderStatus != null) {
            builder.and(order.orderStatus.eq(orderStatus));
        }

        List<Order> results = queryFactory
                .selectFrom(order)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(QueryDslUtil.getAllOrderSpecifierArr(pageable, order))
                .fetch();
        long total = queryFactory
                .selectFrom(order)
                .where(builder)
                .fetchCount();

        return PageableExecutionUtils.getPage(results, pageable, () -> total);
    }
}
