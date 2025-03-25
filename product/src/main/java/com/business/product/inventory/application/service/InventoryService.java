package com.business.product.inventory.application.service;

import com.business.product.inventory.application.dto.request.UpdateInventoryRequestDto;
import com.business.product.inventory.application.dto.response.InventoryResponseDto;
import com.business.product.inventory.application.mapper.InventoryMapper;
import com.business.product.inventory.domain.entity.Inventory;
import com.business.product.inventory.domain.repository.InventoryRepository;
import com.business.product.product.domain.entity.Product;
import com.business.product.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void createInventory(UUID productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        Inventory inventory = Inventory.builder()
                .productId(productId)
                .productQuantity(quantity)
                .build();

        inventoryRepository.save(inventory);
    }

    @Transactional
    public InventoryResponseDto updateInventory(UpdateInventoryRequestDto dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        Inventory inventory = inventoryRepository.findById(dto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("재고를 찾을 수 없습니다."));

        inventory.updateQuantity(dto.getProductQuantity());

        InventoryResponseDto.ProductData productData = InventoryMapper.toProductData(product, inventory);

        return InventoryResponseDto.builder()
                .product(productData)
                .build();
    }
}