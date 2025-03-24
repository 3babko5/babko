package com.business.user.deliverydriver.presentation.controller;

import com.business.user.deliverydriver.application.dto.mapper.DeliveryDriverResponseMapper;
import com.business.user.deliverydriver.application.dto.request.AssignDeliveryDriverRequestDto;
import com.business.user.deliverydriver.application.dto.request.CreateDeliveryDriverRequestDto;
import com.business.user.deliverydriver.application.dto.request.DeliveryDriverSearchRequestDto;
import com.business.user.deliverydriver.application.dto.response.AssignDeliveryDriverResponseDto;
import com.business.user.deliverydriver.application.dto.response.DeliveryDriverDetailResponseDto;
import com.business.user.deliverydriver.application.dto.response.DeliveryDriverListResponseDto;
import com.business.user.deliverydriver.application.dto.response.DeliveryDriverResponseDto;
import com.business.user.deliverydriver.application.service.DeliveryDriverService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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

    @PutMapping("/{deliveryRouteId}/cancel")
    public ResponseEntity<Void> cancelDriverStatus(@PathVariable("deliveryRouteId") UUID deliveryRouteId) {

        deliveryDriverService.cancelDriverStatus(deliveryRouteId);
        return ResponseEntity.ok().build();
    }
}

