package com.business.product.inventory.application.mapper;

import com.business.product.inventory.application.dto.request.UpdateInventoryRequestDto;
import com.business.product.inventory.application.dto.response.InventoryResponseDto;
import com.business.product.inventory.domain.entity.Inventory;
import com.business.product.product.domain.entity.Product;

public class InventoryMapper {

    public static Inventory toEntity(UpdateInventoryRequestDto dto) {
        return Inventory.builder()
                .productQuantity(dto.getProductQuantity())
                .build();
    }

    public static InventoryResponseDto.ProductData toProductData(Product product, Inventory inventory) {
        return InventoryResponseDto.ProductData.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productQuantity(String.valueOf(inventory.getProductQuantity()))
                .companyId(product.getCompanyId())
                .build();
    }
}