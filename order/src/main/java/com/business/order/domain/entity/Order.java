package com.business.order.domain.entity;

import com.business.common.domain.entity.BaseDataEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "p_orders")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends BaseDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "supplier_id", nullable = false)
    private UUID supplierId;

    @Column(name = "receiver_id", nullable = false)
    private UUID receiverId;

    @Column(name = "company_address", nullable = false)
    private String companyAddress;

    @Column(name = "delivery_id")
    private UUID deliveryId;

    @Column(name = "supplier_hub_id", nullable = false)
    private UUID supplierHubId;

    @Column(name = "receiver_hub_id", nullable = false)
    private UUID receiverHubId;

    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderItem> orderItems = new ArrayList<>();
}
