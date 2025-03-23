package com.business.product.inventory.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "p_products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productId;

    @Column(nullable = false)
    private Integer productQuantity;

    @Builder
    public Inventory(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    public void updateQuantity(Integer newQuantity) {
        this.productQuantity = newQuantity;
    }
}
