package com.business.user.deliverydriver.presentation.controller;

import com.business.common.aop.RoleCheck;
import com.business.user.deliverydriver.application.dto.mapper.DeliveryDriverResponseMapper;
import com.business.user.deliverydriver.application.dto.request.AssignDeliveryDriverRequestDto;
import com.business.user.deliverydriver.application.dto.request.CreateDeliveryDriverRequestDto;
import com.business.user.deliverydriver.application.dto.request.DeliveryDriverSearchRequestDto;
import com.business.user.deliverydriver.application.dto.request.StatusUpdateRequestDto;
import com.business.user.deliverydriver.application.dto.response.AssignDeliveryDriverResponseDto;
import com.business.user.deliverydriver.application.dto.response.DeliveryDriverDetailResponseDto;
import com.business.user.deliverydriver.application.dto.response.DeliveryDriverListResponseDto;
import com.business.user.deliverydriver.application.dto.response.DeliveryDriverResponseDto;
import com.business.user.deliverydriver.application.dto.response.DriverStatusUpdateResponseDto;
import com.business.user.deliverydriver.application.service.DeliveryDriverService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/delivery-drivers")
@Slf4j
@RequiredArgsConstructor
public class DeliveryDriverController {

    private final DeliveryDriverService deliveryDriverService;

    @RoleCheck(roles = {"ROLE_MASTER", "ROLE_HUB"})
    @PostMapping
    public ResponseEntity<DeliveryDriverResponseDto> createDeliveryDriver(
        @RequestBody CreateDeliveryDriverRequestDto request,
        @RequestHeader("X-client-userId") Long userId,
        @RequestHeader("X-client-role") String role) {

        DeliveryDriverResponseDto responseDto = deliveryDriverService.createDeliveryDriver(request, userId, role);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @RoleCheck(roles = {"ROLE_MASTER", "ROLE_HUB"})
    @PostMapping("/assign")
    public ResponseEntity<Long> assignDriversForDelivery(
            @RequestParam("delivery_id") UUID deliveryId,
        @RequestHeader("X-client-userId") Long userId,
            @RequestHeader("X-client-role") String role    ) {

        List<AssignDeliveryDriverResponseDto> assignedDrivers =
            deliveryDriverService.assignDriversForDelivery(deliveryId, userId, role);

        return ResponseEntity.ok(assignedDrivers.get(0).getDeliveryDriverId());
    }

    @RoleCheck(roles = {"ROLE_MASTER", "ROLE_HUB", "ROLE_DELIVERY"})
    @GetMapping
    public ResponseEntity<DeliveryDriverListResponseDto<DeliveryDriverResponseDto>> getDrivers(
        DeliveryDriverSearchRequestDto request,
        @RequestHeader("X-client-userId") Long userId,
        @RequestHeader("X-client-role") String role) {

        Page<DeliveryDriverResponseDto> driverPage = deliveryDriverService.getDrivers(request, userId, role);
        return ResponseEntity.ok(DeliveryDriverResponseMapper.toPageListResponse(driverPage));
    }

    @RoleCheck(roles = {"ROLE_MASTER", "ROLE_HUB", "ROLE_DELIVERY"})
    @GetMapping("/{deliveryDriverId}")
    public ResponseEntity<DeliveryDriverDetailResponseDto> getDeliveryDriverDetail(
        @PathVariable Long deliveryDriverId,
        @RequestHeader("X-client-userId") Long userId,
        @RequestHeader("X-client-role") String role) {

        DeliveryDriverDetailResponseDto response = deliveryDriverService.getDriverByDriverId(deliveryDriverId, userId, role);
        return ResponseEntity.ok(response);
    }

    @RoleCheck(roles = {"ROLE_MASTER", "ROLE_HUB"})
    @PutMapping("/{deliveryRouteId}/status")
    public ResponseEntity<DriverStatusUpdateResponseDto> updateDriverStatus(
        @PathVariable("deliveryRouteId") UUID deliveryRouteId,
        @RequestBody StatusUpdateRequestDto request,
        @RequestHeader("X-client-userId") Long userId,
        @RequestHeader("X-client-role") String role) {

        log.info(request.getDeliveryRouteStatus());
        DriverStatusUpdateResponseDto response =
            deliveryDriverService.updateDriverStatus(deliveryRouteId, request, userId, role);
        return ResponseEntity.ok(response);
    }

    @RoleCheck(roles = {"ROLE_MASTER", "ROLE_HUB"})
    @PutMapping("/{deliveryRouteId}/cancel")
    public ResponseEntity<Void> cancelDriverStatus(
        @PathVariable("deliveryRouteId") UUID deliveryRouteId,
        @RequestHeader("X-client-userId") Long userId,
        @RequestHeader("X-client-role") String role) {

        deliveryDriverService.cancelDriverStatus(deliveryRouteId, userId, role);
        return ResponseEntity.ok().build();
    }
}
