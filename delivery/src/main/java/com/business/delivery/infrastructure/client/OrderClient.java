package com.business.delivery.infrastructure.client;

import java.util.Map;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service", url = "${order.service.url}")
public interface OrderClient {

    @GetMapping("/api/v1/orders/{orderId}")
    Map<String, Object> getOrder(@PathVariable("orderId") UUID orderId);
}
