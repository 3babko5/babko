package com.business.order.domain.entity;

public enum OrderStatus {
    CREATED,        // 주문 생성됨 (기본)
    COMPLETED,      // 배송까지 완료됨
    CANCELED        // 주문 취소됨
}
