package com.business.product.product.domain.repository;

import com.business.product.product.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(UUID productId);
    Page<Product> search(String productName, UUID companyId, UUID productId, Pageable pageable);
}