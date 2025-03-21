package com.business.product.product.presentation;

import com.business.product.product.application.dto.request.CreateProductRequestDto;
import com.business.product.product.application.dto.response.CreateProductResponseDto;
import com.business.product.product.application.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/health-check")
    ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Health Check OK");
    }

    @PostMapping
    public ResponseEntity<CreateProductResponseDto> createProduct(@RequestBody CreateProductRequestDto createProductRequestDto) {
        CreateProductResponseDto response = productService.createProduct(createProductRequestDto);
        return ResponseEntity.status(201).body(response);
    }
}