package com.business.product.inventory.presentation;

import com.business.common.aop.RoleCheck;
import com.business.product.inventory.application.dto.request.UpdateInventoryRequestDto;
import com.business.product.inventory.application.dto.response.InventoryResponseDto;
import com.business.product.inventory.application.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inventories")
public class InventoryController {

    private final InventoryService inventoryService;

    @PutMapping
    @RoleCheck(roles = {"ROLE_MASTER", "ROLE_HUB", "ROLE_COMPANY"})
    public ResponseEntity<InventoryResponseDto> updateInventory(
            @RequestBody UpdateInventoryRequestDto dto,
            @RequestHeader("X-client-userId") Long userId
    ) {
        InventoryResponseDto response = inventoryService.updateInventory(dto);
        return ResponseEntity.ok(response);
    }
}