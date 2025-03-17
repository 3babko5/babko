package com.business.delivery.presentation.controller;

import com.business.delivery.application.dto.request.CreateDeliveryRouteRequestDto;
import com.business.delivery.application.dto.response.DeliveryRouteResponseDto;
import com.business.delivery.application.service.DeliveryRouteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/delivery-routes")
public class DeliveryRouteController {

  private final DeliveryRouteService deliveryRouteService;

  @PostMapping
  public ResponseEntity<DeliveryRouteResponseDto> createDeliveryRoute(
      @Valid @RequestBody CreateDeliveryRouteRequestDto request
  ) {
    DeliveryRouteResponseDto response = deliveryRouteService.createDeliveryRoute(request);
    return ResponseEntity.ok(response);
  }
}
