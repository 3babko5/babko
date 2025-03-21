package com.business.product.product.domain.repository;

import com.business.product.product.domain.entity.Product;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(UUID productId);
}
