package com.business.order.domain.entity;

public enum DeliveryStatus {
    CANCELED,
    WAITING_AT_HUB,
    IN_TRANSIT_TO_HUB,
    ARRIVED_AT_HUB,
    OUT_FOR_DELIVERY,
    DELIVERED
}
