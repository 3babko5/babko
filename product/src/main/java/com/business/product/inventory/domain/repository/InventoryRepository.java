package com.business.product.inventory.domain.repository;

import com.business.product.inventory.domain.entity.Inventory;

import java.util.Optional;
import java.util.UUID;

public interface InventoryRepository {
    Inventory save(Inventory inventory);
    Optional<Inventory> findById(UUID productId);
}