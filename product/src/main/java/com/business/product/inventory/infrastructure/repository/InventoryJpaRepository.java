package com.business.product.inventory.infrastructure.repository;

import com.business.product.inventory.domain.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InventoryJpaRepository extends JpaRepository<Inventory, UUID> {
}