package com.business.order.infrastructure.client;

import com.business.order.infrastructure.dto.request.OrderDeliveryRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "delivery-service", url = "http://delivery-service/api/v1/deliveries")
public interface DeliveryFeignClient {

    @PostMapping
    boolean createDelivery(@RequestBody OrderDeliveryRequestDto request);

    @GetMapping("/{orderId}/deliveryStatus")//url 확인 필요
    String getDeliveryStatus(@PathVariable("orderId") UUID orderId);
}
