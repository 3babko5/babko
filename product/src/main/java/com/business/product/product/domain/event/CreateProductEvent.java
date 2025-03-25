package com.business.product.product.domain.event;

import lombok.Getter;

import java.util.UUID;

@Getter
public class CreateProductEvent {

    private final UUID productId;
    private final Integer productQuantity;

    public CreateProductEvent(UUID productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}