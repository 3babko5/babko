package com.business.user.deliverydriver.infrastructure.client;

import com.business.user.deliverydriver.infrastructure.dto.response.DeliveryClientResponseDto;
import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "delivery-service", url = "${delivery.service.url}")
public interface DeliveryClient {

  @GetMapping("/api/v1/deliveries/{deliveryId}")
  List<DeliveryClientResponseDto> getRoutesByDeliveryId(@PathVariable UUID deliveryId);
}
