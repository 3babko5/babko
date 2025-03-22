package com.business.order.domain.repository;

import com.business.order.domain.entity.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Order save(Order order);

    Order findByOrderIdWithItems(UUID orderId);

    Optional<Order> findById(UUID orderId);
}
