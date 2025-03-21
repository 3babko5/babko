package com.business.order.infrastructure.client;

import com.business.order.infrastructure.dto.response.ProductDetailResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "product-service", url = "http://delivery-service/api/v1/products")
public interface ProductFeignClient {

    @GetMapping("/{productId}")//url 확인 필요
    ProductDetailResponseDto getProductDetail(@PathVariable("productId") UUID productId);

}
