package com.business.product.product.presentation;

import com.business.common.infrastructure.util.JpaUtil;
import com.business.product.product.application.dto.request.CreateProductRequestDto;
import com.business.product.product.application.dto.request.SearchProductRequestDto;
import com.business.product.product.application.dto.response.CreateProductResponseDto;
import com.business.product.product.application.dto.response.SearchProductResponseDto;
import com.business.product.product.application.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<CreateProductResponseDto> createProduct(@RequestBody CreateProductRequestDto createProductRequestDto) {
        CreateProductResponseDto response = productService.createProduct(createProductRequestDto);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<SearchProductResponseDto> searchProducts(
            @ModelAttribute SearchProductRequestDto request) {
        Pageable pageable = JpaUtil.getNormalPageable(
                request.getPage(), request.getSize(), request.getOrderBy(), request.getSort()
        );
        final SearchProductResponseDto response = productService.searchProducts(request, pageable);
        return ResponseEntity.ok(response);
    }
}