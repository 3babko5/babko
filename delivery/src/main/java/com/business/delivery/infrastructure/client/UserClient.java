package com.business.delivery.infrastructure.client;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "${user.service.url}")
public interface UserClient {

  @PostMapping("/api/v1/delivery-drivers/assign")
  Long assignDeliveryDriver(
      @RequestParam("delivery_id") UUID deliveryId,
      @RequestHeader("X-client-userId") Long userId,
      @RequestHeader("X-client-role") String role
  );

  @PatchMapping("/drivers/{deliveryRouteId}/status")
  void updateDriverStatus(
      @PathVariable("deliveryRouteId") UUID deliveryRouteId,
      @RequestHeader("X-client-userId") Long userId,
      @RequestHeader("X-client-role") String role
  );

  @PatchMapping("/api/v1/delivery-drivers/{deliveryRouteId}/cancel")
  void cancelDriverStatus(
      @PathVariable("deliveryRouteId") UUID deliveryRouteId,
      @RequestHeader("X-client-userId") Long userId,
      @RequestHeader("X-client-role") String role
  );
}
