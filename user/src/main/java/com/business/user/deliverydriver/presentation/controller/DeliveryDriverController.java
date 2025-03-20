package com.business.user.deliverydriver.presentation.controller;

import com.business.user.deliverydriver.application.dto.request.AssignDeliveryDriverRequestDto;
import com.business.user.deliverydriver.application.dto.request.CreateDeliveryDriverRequestDto;
import com.business.user.deliverydriver.application.dto.response.AssignDeliveryDriverResponseDto;
import com.business.user.deliverydriver.application.dto.response.DeliveryDriverResponseDto;
import com.business.user.deliverydriver.application.service.DeliveryDriverService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/delivery-drivers")
@RequiredArgsConstructor
public class DeliveryDriverController {

  private final DeliveryDriverService deliveryDriverService;

  @PostMapping
  public ResponseEntity<DeliveryDriverResponseDto> createDeliveryDriver(@RequestBody CreateDeliveryDriverRequestDto request) {

    DeliveryDriverResponseDto responseDto = deliveryDriverService.createDeliveryDriver(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
  }

  @PostMapping("/assign")
  public ResponseEntity<List<AssignDeliveryDriverResponseDto>> assignDriversForDelivery(
      @RequestBody AssignDeliveryDriverRequestDto request
  ) {
    List<AssignDeliveryDriverResponseDto> assignedDrivers =
        deliveryDriverService.assignDriversForDelivery(request.getDeliveryId());

    return ResponseEntity.ok(assignedDrivers);
  }
}
