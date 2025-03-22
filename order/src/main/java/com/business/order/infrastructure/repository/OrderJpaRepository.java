package com.business.order.infrastructure.repository;

import com.business.order.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderJpaRepository extends JpaRepository<Order, UUID> {

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems WHERE o.orderId = :orderId")
    Order findByOrderIdWithItems(@Param("orderId") UUID orderId);

    List<Order> orderId(UUID orderId);

    Optional<Order> findById(UUID orderId);
}