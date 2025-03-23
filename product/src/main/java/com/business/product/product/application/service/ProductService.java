package com.business.product.product.application.service;

import com.business.product.product.application.dto.request.CreateProductRequestDto;
import com.business.product.product.application.dto.request.SearchProductRequestDto;
import com.business.product.product.application.dto.response.CreateProductResponseDto;
import com.business.product.product.application.dto.response.SearchProductResponseDto;
import com.business.product.product.application.mapper.ProductMapper;
import com.business.product.product.domain.entity.Product;
import com.business.product.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public CreateProductResponseDto createProduct(CreateProductRequestDto dto) {
        Product product = ProductMapper.toEntity(dto);
        product.createdBy(1L); // 임시
        Product saved = productRepository.save(product);
        return ProductMapper.toResponseDto(saved);
    }

    @Transactional(readOnly = true)
    public SearchProductResponseDto searchProducts(SearchProductRequestDto request, Pageable pageable) {
        Page<Product> productPage = productRepository.search(
                request.getProductName(),
                request.getCompanyId(),
                pageable
        );
        return ProductMapper.toSearchResponseDto(productPage);
    }
}