package com.business.order.infrastructure.client;

import com.business.order.application.dto.response.ProductDetailResponseDto;
import com.business.order.infrastructure.dto.response.GetProductInfoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "product-service", url = "http://product-service/api/v1/products")
public interface ProductFeignClient {

    @GetMapping
    GetProductInfoResponseDto searchProduct(
            @RequestParam("productId") UUID productId,
            @RequestParam(value = "productName", required = false) String productName,
            @RequestParam(value = "companyId", required = false) UUID companyId,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("orderBy") String orderBy,
            @RequestParam("sort") String sort
    );

}
