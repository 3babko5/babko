package com.business.product.inventory.infrastructure.repository;

import com.business.product.inventory.domain.entity.Inventory;
import com.business.product.inventory.domain.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class InventoryRepositoryImpl implements InventoryRepository {

    private final InventoryJpaRepository inventoryJpaRepository;

    @Override
    public Inventory save(Inventory inventory) {
        return inventoryJpaRepository.save(inventory);
    };

    @Override
    public Optional<Inventory> findById(UUID productId) {
        return inventoryJpaRepository.findById(productId);
    }
}