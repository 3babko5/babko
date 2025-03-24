package com.business.delivery.presentation.controller;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping
    public ResponseEntity<DeliveryResponseDto> createDelivery(
        @Valid @RequestBody CreateDeliveryRequestDto request
    ) {

        DeliveryResponseDto response = deliveryService.createDelivery(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<DeliveryPageListResponseDto<DeliveryPageResponseDto>> getDeliveries(
        SearchRequestDto request) {

        Page<DeliveryPageResponseDto> deliveryPage = deliveryService.getDeliveries(request);
        return ResponseEntity.ok(DeliveryResponseMapper.toPageListResponse(deliveryPage));
    }

    @GetMapping("/{deliveryId}")
    public ResponseEntity<DeliveryDetailResponseDto> getDeliveryDetail(@PathVariable("deliveryId") UUID deliveryId) {

        DeliveryDetailResponseDto response = deliveryService.getDeliveryByDeliveryId(deliveryId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{deliveryId}/status")
    public ResponseEntity<DeliveryStatusUpdateResponseDto> updateDeliveryStatus(
        @PathVariable("deliveryId") UUID deliveryId,
        @Valid @RequestBody StatusUpdateRequestDto request) {

        DeliveryStatusUpdateResponseDto response = deliveryService.updateDeliveryStatus(deliveryId,
            request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{deliveryId}/cancel")
    public ResponseEntity<Void> cancelDelivery(@PathVariable("deliveryId") UUID deliveryId) {

        deliveryService.cancelDelivery(deliveryId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{deliveryId}")
    public ResponseEntity<Void> deleteByDeliveryId(@PathVariable("deliveryId") UUID deliveryId,
        @RequestParam Long deletedBy) {

        deliveryService.deleteByDeliveryId(deliveryId, deletedBy);
        return ResponseEntity.noContent().build();
    }
}