package com.business.order.domain.repository;

import com.business.order.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface OrderRepository {
    Order save(Order order);
}
