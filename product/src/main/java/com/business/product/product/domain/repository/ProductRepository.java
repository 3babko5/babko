package com.business.product.product.domain.repository;

import com.business.product.product.domain.entity.Product;

public interface ProductRepository {
    Product save(Product product);
}
