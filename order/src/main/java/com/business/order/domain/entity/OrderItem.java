package com.business.order.domain.entity;

import com.business.common.domain.entity.BaseDataEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "p_order_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends BaseDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_item_id", updatable = false, nullable = false)
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
    public static OrderItem create(Order order, UUID productId, Integer orderItemAmount, Long orderItemPrice) {
        return OrderItem.builder()
                .order(order)
                .productId(productId)
                .orderItemAmount(orderItemAmount)
                .orderItemPrice(orderItemPrice)
                .build();
    }

}
