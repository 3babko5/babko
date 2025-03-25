package com.business.order.infrastructure.client;

import com.business.order.application.dto.response.DeliveryStatusForOrderDto;
import com.business.order.infrastructure.dto.request.CreateDeliveryRequestDto;
import com.business.order.infrastructure.dto.request.DeliverySearchForOrderRequestDto;
import com.business.order.infrastructure.dto.response.DeliveryIdResponseDto;
import com.business.order.infrastructure.dto.response.DeliveryListForOrderResponseDto;
import com.business.order.infrastructure.dto.response.GetDeliveryCreateResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "delivery-service", url = "http://localhost:8088")
public interface DeliveryFeignClient {

    @PostMapping("/api/v1/deliveries")
    GetDeliveryCreateResponseDto createDelivery(
            @RequestHeader("X-client-userId") Long userId,
            @RequestHeader("X-client-role") String role,
            @RequestBody CreateDeliveryRequestDto request);

    //주문 취소 시 배송 상태 확인 요청
    @GetMapping("/api/v1/deliveries")
    DeliveryListForOrderResponseDto<DeliveryStatusForOrderDto> getDeliveries(
            @SpringQueryMap DeliverySearchForOrderRequestDto request);

    //주문 취소 시 배송 취소 요청
    @PatchMapping("/api/v1/deliveries/{deliveryId}/cancel")
    void cancelDelivery(@PathVariable("deliveryId") UUID deliveryId);

    @GetMapping("/deliveries")
    List<DeliveryIdResponseDto> getDeliveryInfo();

    @DeleteMapping("/{deliveryId}")
    void deleteByDeliveryId(
            @PathVariable("deliveryId") UUID deliveryId,
            @RequestParam("deletedBy") Long deletedBy
    );
}
