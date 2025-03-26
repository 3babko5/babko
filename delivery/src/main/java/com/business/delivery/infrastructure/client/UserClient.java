package com.business.delivery.infrastructure.client;

import java.util.UUID;

import com.business.delivery.application.dto.request.StatusUpdateRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service")
public interface UserClient {

  @PostMapping("/api/v1/delivery-drivers/assign")
  Long assignDeliveryDriver(
      @RequestParam("delivery_id") UUID deliveryId,
      @RequestHeader("X-client-userId") Long userId,
      @RequestHeader("X-client-role") String role
  );

  @PutMapping("/api/v1/delivery-drivers/{deliveryRouteId}/status")
  void updateDriverStatus(
      @PathVariable("deliveryRouteId") UUID deliveryRouteId,
      @RequestBody StatusUpdateRequestDto request,
      @RequestHeader("X-client-userId") Long userId,
      @RequestHeader("X-client-role") String role
  );

  @PutMapping("/api/v1/delivery-drivers/{deliveryRouteId}/cancel")
  void cancelDriverStatus(
      @PathVariable("deliveryRouteId") UUID deliveryRouteId,
      @RequestHeader("X-client-userId") Long userId,
      @RequestHeader("X-client-role") String role
  );
}
