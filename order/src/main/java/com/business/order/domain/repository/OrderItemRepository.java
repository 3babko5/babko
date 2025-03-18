package com.business.order.domain.repository;

import com.business.order.domain.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
    //특정 주문의 주문 아이템 조회
    List<OrderItem> findByOrderOrderId(UUID orderId);
}
