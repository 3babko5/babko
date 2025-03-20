package com.business.order.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@Entity
@Table(name = "p_order_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_item_id")
    private UUID orderItemId;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "supplier_id", nullable = false)
    private UUID supplierId;

    @Column(name = "order_item_amount", nullable = false)
    private Integer orderItemAmount;

    @Column(name = "order_item_price", nullable = false)
    private Long orderItemPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Builder
    public OrderItem(Order order, UUID productId, UUID supplierId, Integer orderItemAmount,
                      Long orderItemPrice) {
        this.order = order;
        this.productId = productId;
        this.supplierId = supplierId;
        this.orderItemAmount = orderItemAmount;
        this.orderItemPrice = orderItemPrice;
    }

    public static OrderItem create(Order order, UUID productId, UUID supplierId,
                                   Integer orderItemAmount, Long orderItemPrice) {
        return OrderItem.builder()
                .order(order)
                .productId(productId)
                .supplierId(supplierId)
                .orderItemAmount(orderItemAmount)
                .orderItemPrice(orderItemPrice)
                .build();
    }

}
