package com.business.delivery.infrastructure.client;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service", url = "http://user-service/api/v1/users")
public interface UserClient {

  @PostMapping("/api/v1/delivery-drivers/assign")
  Long assignDeliveryDriver(@RequestParam("delivery_id") UUID deliveryId);

  @GetMapping("/{userId}/user-id")
  UUID getSlackIdByUserId(@PathVariable UUID userId);
}
