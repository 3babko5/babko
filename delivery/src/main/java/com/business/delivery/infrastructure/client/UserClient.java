package com.business.delivery.infrastructure.client;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service", url = "${user.service.url}")
public interface UserClient {

  @PostMapping("/api/v1/delivery-drivers/assign")
  Long assignDeliveryDriver(@RequestParam("delivery_id") UUID deliveryId);

  @PutMapping("/api/v1/delivery-drivers/{deliveryRouteId}/cancel")
  void cancelDriverStatus(@PathVariable("deliveryRouteId") UUID deliveryRouteId);
}
