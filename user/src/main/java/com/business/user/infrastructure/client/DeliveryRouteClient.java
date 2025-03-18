package com.business.user.infrastructure.client;

import com.business.user.infrastructure.dto.response.DeliveryRouteClientResponseDto;
import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "delivery-service", url = "${delivery.service.url}")
public interface DeliveryRouteClient {

  @GetMapping("/api/v1/delivery-routes/{deliveryId}")
  List<DeliveryRouteClientResponseDto> getRoutesByDeliveryId(@PathVariable UUID deliveryId);
}
