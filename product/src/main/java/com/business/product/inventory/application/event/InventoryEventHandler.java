package com.business.product.inventory.application.event;

import com.business.product.inventory.application.service.InventoryService;
import com.business.product.product.domain.event.CreateProductEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InventoryEventHandler {

    private final InventoryService inventoryService;

    @EventListener
    @Transactional
    public void handle(CreateProductEvent event) {
        inventoryService.createInventory(event.getProductId(), event.getProductQuantity());
    }
}