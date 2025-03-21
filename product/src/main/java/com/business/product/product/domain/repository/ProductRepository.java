package com.business.product.product.domain.repository;

import com.business.product.product.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductRepository {
    Product save(Product product);
    Page<Product> search(String productName, UUID companyId, Pageable pageable);
}