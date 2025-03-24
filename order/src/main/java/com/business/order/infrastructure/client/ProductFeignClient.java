package com.business.order.infrastructure.client;

import com.business.order.application.dto.response.ProductDetailResponseDto;
import com.business.order.infrastructure.dto.queryDto.SearchProductQueryDto;
import com.business.order.infrastructure.dto.response.GetProductInfoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "product-service", url = "http://localhost:8081")
public interface ProductFeignClient {

    @GetMapping("/api/v1/products")
    GetProductInfoResponseDto searchProduct(
            @SpringQueryMap SearchProductQueryDto request
    );

}
