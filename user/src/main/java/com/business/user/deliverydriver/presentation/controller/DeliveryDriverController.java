package com.business.user.deliverydriver.presentation.controller;

import com.business.user.deliverydriver.application.dto.mapper.DeliveryDriverResponseMapper;
import com.business.user.deliverydriver.application.dto.request.AssignDeliveryDriverRequestDto;
import com.business.user.deliverydriver.application.dto.request.CreateDeliveryDriverRequestDto;
import com.business.user.deliverydriver.application.dto.request.DeliveryDriverSearchRequestDto;
import com.business.user.deliverydriver.application.dto.response.AssignDeliveryDriverResponseDto;
import com.business.user.deliverydriver.application.dto.response.DeliveryDriverDetailResponseDto;


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
    public ResponseEntity<List<AssignDeliveryDriverResponseDto>> assignDriversForDelivery(@RequestBody AssignDeliveryDriverRequestDto request) {
        
        List<AssignDeliveryDriverResponseDto> assignedDrivers = deliveryDriverService.assignDriversForDelivery(request.getDeliveryId());
        return ResponseEntity.ok(assignedDrivers);
    }

    @GetMapping
    public ResponseEntity<DeliveryDriverListResponseDto<DeliveryDriverResponseDto>> getDrivers(DeliveryDriverSearchRequestDto request) {

        Page<DeliveryDriverResponseDto> driverPage = deliveryDriverService.getDrivers(request);
        return ResponseEntity.ok(DeliveryDriverResponseMapper.toPageListResponse(driverPage));
    }

    @GetMapping("/{deliveryDriverId}")
    public ResponseEntity<DeliveryDriverDetailResponseDto> getDeliveryDriverDetail(@PathVariable Long deliveryDriverId) {

        DeliveryDriverDetailResponseDto response = deliveryDriverService.getDriverByDriverId(deliveryDriverId);
        return ResponseEntity.ok(response);
    }
}

