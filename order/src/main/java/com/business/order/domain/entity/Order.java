package com.business.order.domain.entity;

import com.business.common.domain.entity.BaseDataEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Entity
@Table(name = "p_orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "receiver_id", nullable = false)
    private UUID receiverId;

    @Column(name = "delivery_address", nullable = false)
    private String deliveryAddress; //배송지 주소

    @Column(name = "delivery_id")
    private UUID deliveryId;

    @Column(name = "origin_hub_id", nullable = false)
    private UUID originHubId; //출발허브

    @Column(name = "destination_hub_id", nullable = false)
    private UUID destinationHubId; //도착허브

    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Builder
    public Order(Long userId, UUID receiverId, String deliveryAddress,
                 UUID originHubId, UUID destinationHubId, Integer totalPrice) {
        this.userId = userId;
        this.receiverId = receiverId;
        this.deliveryAddress = deliveryAddress;
        this.originHubId = originHubId;
        this.destinationHubId = destinationHubId;
        this.totalPrice = totalPrice;
        createdBy(userId);
    }

    public static Order create(Long userId, UUID receiverId, String deliveryAddress,
                               UUID originHubId, UUID destinationHubId, Integer totalPrice) {
        Order order = Order.builder()
                .userId(userId)
                .receiverId(receiverId)
                .deliveryAddress(deliveryAddress)
                .originHubId(originHubId)
                .destinationHubId(destinationHubId)
                .totalPrice(totalPrice)
                .build();
        order.createdBy(userId);
        return order;
    }

    public void addOrderItems(List<OrderItem> items) {
        this.orderItems.addAll(items);
    }

    public void cancelOrder(Long userId) {
        this.orderStatus = OrderStatus.CANCELED;
        updatedBy(userId);
    }

    public void updateDeliveryId(UUID deliveryId) {
        this.deliveryId = deliveryId;
    }
}
