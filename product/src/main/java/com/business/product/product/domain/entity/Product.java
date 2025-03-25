package com.business.product.product.domain.entity;

import com.business.common.domain.entity.BaseDataEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "p_products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productId;

    @Column(nullable = false)
    private String productName;

    @Min(0)
    @Column(nullable = false)
    private Integer productPrice;

    @Column(nullable = false)
    private UUID companyId;

    @Builder
    public Product(UUID productId, String productName, Integer productPrice, UUID companyId) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.companyId = companyId;
    }
}
