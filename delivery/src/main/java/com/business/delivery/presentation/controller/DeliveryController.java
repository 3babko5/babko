package com.business.delivery.presentation.controller;

import com.business.delivery.application.dto.mapper.DeliveryResponseMapper;
import com.business.delivery.application.dto.request.CreateDeliveryRequestDto;
import com.business.delivery.application.dto.request.DeliverySearchRequestDto;
import com.business.delivery.application.dto.response.DeliveryDetailResponseDto;
import com.business.delivery.application.dto.response.DeliveryPageResponseDto;
import com.business.delivery.application.dto.response.DeliveryPageListResponseDto;
import com.business.delivery.application.dto.response.DeliveryResponseDto;
import com.business.delivery.application.service.DeliveryService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResponseEntity<DeliveryPageListResponseDto<DeliveryPageResponseDto>> getDeliveries(DeliverySearchRequestDto request) {

        Page<DeliveryPageResponseDto> deliveryPage = deliveryService.getDeliveries(request);
        return ResponseEntity.ok(DeliveryResponseMapper.toPageListResponse(deliveryPage));
    }

    @GetMapping("/{deliveryId}")
    public ResponseEntity<DeliveryDetailResponseDto> getDeliveryDetail(@PathVariable UUID deliveryId) {

        DeliveryDetailResponseDto response = deliveryService.getDeliveryByDeliveryId(deliveryId);
        return ResponseEntity.ok(response);
    }
}