package com.business.order.domain.repository;

import com.business.order.domain.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

public interface OrderItemRepository {
    List<OrderItem> saveAll(List<OrderItem> orderItems);
}
