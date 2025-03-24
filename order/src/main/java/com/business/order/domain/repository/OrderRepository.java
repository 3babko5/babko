package com.business.order.domain.repository;

import com.business.order.domain.entity.Order;
import com.business.order.domain.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {

    Page<Order> search(UUID orderId, OrderStatus orderStatus, Pageable pageable);

    Order save(Order order);

    Order findByOrderIdWithItems(UUID orderId);

    Optional<Order> findById(UUID orderId);
}
