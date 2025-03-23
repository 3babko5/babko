package com.business.order.infrastructure.repository;

import com.business.order.domain.entity.Order;
import com.business.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public Order saveAndFlush(Order order) {
        return orderJpaRepository.saveAndFlush(order);
    }

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
}
