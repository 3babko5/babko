package com.business.delivery.infrastructure.client;

import java.util.Map;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "order-service")
public interface OrderClient {

    @GetMapping("/api/v1/orders/{orderId}")
    Map<String, Object> getOrder(
        @PathVariable("orderId") UUID orderId,
        @RequestHeader("X-client-userId") Long userId,
        @RequestHeader("X-client-role") String role
    );

    @PutMapping("/api/v1/orders/{orderId}/completed")
    void completeOrder(
        @PathVariable("orderId") UUID orderId,
        @RequestHeader("X-client-userId") Long userId,
        @RequestHeader("X-client-role") String role
    );
}
