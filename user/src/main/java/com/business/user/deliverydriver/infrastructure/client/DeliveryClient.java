package com.business.user.deliverydriver.infrastructure.client;

import com.business.user.deliverydriver.infrastructure.dto.response.DeliveryClientResponseDto;
import java.util.List;
import java.util.UUID;
import com.business.user.deliverydriver.infrastructure.dto.response.DeliveryResponseWrapperDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "delivery-service")
public interface DeliveryClient {

  @GetMapping("/api/v1/deliveries/{deliveryId}")
  DeliveryResponseWrapperDto getRoutesByDeliveryId(
          @PathVariable("deliveryId") UUID deliveryId,
          @RequestHeader("X-client-userId") Long userId,
          @RequestHeader("X-client-role") String role
  );

}
