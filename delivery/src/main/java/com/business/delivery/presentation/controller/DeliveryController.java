package com.business.delivery.presentation.controller;

import com.business.common.aop.RoleCheck;
import com.business.delivery.application.dto.mapper.DeliveryResponseMapper;
import com.business.delivery.application.dto.request.CreateDeliveryRequestDto;
import com.business.delivery.application.dto.request.SearchRequestDto;
import com.business.delivery.application.dto.request.StatusUpdateRequestDto;
import com.business.delivery.application.dto.response.DeliveryDetailResponseDto;
import com.business.delivery.application.dto.response.DeliveryPageResponseDto;
import com.business.delivery.application.dto.response.DeliveryPageListResponseDto;
import com.business.delivery.application.dto.response.DeliveryResponseDto;
import com.business.delivery.application.dto.response.DeliveryStatusUpdateResponseDto;
import com.business.delivery.application.service.DeliveryService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @RoleCheck(roles = {"ROLE_MASTER"})
    @PostMapping
    public ResponseEntity<DeliveryResponseDto> createDelivery(
        @Valid @RequestBody CreateDeliveryRequestDto request,
        @RequestHeader("X-client-userId") Long userId,
        @RequestHeader("X-client-role") String role
    ) {
        DeliveryResponseDto response = deliveryService.createDelivery(request, userId, role);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @RoleCheck(roles = {"ROLE_MASTER", "ROLE_HUB", "ROLE_DELIVERY", "ROLE_COMPANY"})
    @GetMapping
    public ResponseEntity<DeliveryPageListResponseDto<DeliveryPageResponseDto>> getDeliveries(
        SearchRequestDto request,
        @RequestHeader("X-client-userId") Long userId,
        @RequestHeader("X-client-role") String role
    ) {
        Page<DeliveryPageResponseDto> deliveryPage = deliveryService.getDeliveries(request, userId, role);
        return ResponseEntity.ok(DeliveryResponseMapper.toPageListResponse(deliveryPage));
    }

    @RoleCheck(roles = {"ROLE_MASTER", "ROLE_HUB", "ROLE_DELIVERY", "ROLE_COMPANY"})
    @GetMapping("/{deliveryId}")
    public ResponseEntity<DeliveryDetailResponseDto> getDeliveryDetail(
        @PathVariable("deliveryId") UUID deliveryId,
        @RequestHeader("X-client-userId") Long userId,
        @RequestHeader("X-client-role") String role
    ) {
        DeliveryDetailResponseDto response = deliveryService.getDeliveryByDeliveryId(deliveryId, userId, role);
        return ResponseEntity.ok(response);
    }

    @RoleCheck(roles = {"ROLE_MASTER", "ROLE_HUB", "ROLE_DELIVERY"})
    @PatchMapping("/{deliveryId}/status")
    public ResponseEntity<DeliveryStatusUpdateResponseDto> updateDeliveryStatus(
        @PathVariable("deliveryId") UUID deliveryId,
        @Valid @RequestBody StatusUpdateRequestDto request,
        @RequestHeader("X-client-userId") Long userId,
        @RequestHeader("X-client-role") String role
    ) {
        DeliveryStatusUpdateResponseDto response = deliveryService.updateDeliveryStatus(deliveryId, request, userId, role);
        return ResponseEntity.ok(response);
    }

    @RoleCheck(roles = {"ROLE_MASTER", "ROLE_HUB", "ROLE_DELIVERY"})
    @PatchMapping("/{deliveryId}/cancel")
    public ResponseEntity<Void> cancelDelivery(
        @PathVariable("deliveryId") UUID deliveryId,
        @RequestHeader("X-client-userId") Long userId,
        @RequestHeader("X-client-role") String role
    ) {
        deliveryService.cancelDelivery(deliveryId, userId, role);
        return ResponseEntity.ok().build();
    }

    @RoleCheck(roles = {"ROLE_MASTER", "ROLE_HUB"})
    @DeleteMapping("/{deliveryId}")
    public ResponseEntity<Void> deleteByDeliveryId(
        @PathVariable("deliveryId") UUID deliveryId,
        @RequestParam Long deletedBy,
        @RequestHeader("X-client-userId") Long userId,
        @RequestHeader("X-client-role") String role
    ) {
        deliveryService.deleteByDeliveryId(deliveryId, deletedBy, userId, role);
        return ResponseEntity.noContent().build();
    }
}