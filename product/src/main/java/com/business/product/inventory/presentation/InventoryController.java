package com.business.product.inventory.presentation;

import com.business.product.inventory.application.dto.request.UpdateInventoryRequestDto;
import com.business.product.inventory.application.dto.response.InventoryResponseDto;
import com.business.product.inventory.application.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class InventoryController {

    private final InventoryService inventoryService;

    @PutMapping("/update")
    public ResponseEntity<InventoryResponseDto> updateInventory(@RequestBody UpdateInventoryRequestDto dto) {
        InventoryResponseDto response = inventoryService.updateInventory(dto);
        return ResponseEntity.ok(response);
    }
}