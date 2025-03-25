package com.business.order.presentaion;

import com.business.common.infrastructure.util.JpaUtil;
import com.business.order.application.dto.request.OrderCreateRequestDto;
import com.business.order.application.dto.request.SearchOrderRequestDto;
import com.business.order.application.dto.response.OrderStatusResponseDto;
import com.business.order.application.dto.response.OrderCreateResponseDto;
import com.business.order.application.dto.response.OrderGetResponseDto;
import com.business.order.application.dto.response.SearchOrderResponseDto;
import com.business.order.application.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderCreateResponseDto> createOrder(
            @Valid @RequestBody OrderCreateRequestDto request,
            Long userId
    ) {
        userId = 1L;
        OrderCreateResponseDto response = orderService.createOrder(request, userId);
        return ResponseEntity.ok(response);
    }

    //주문 단일조회
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderGetResponseDto> getOrder(
            @PathVariable UUID orderId) {
        OrderGetResponseDto response = orderService.getOrder(orderId);
        return ResponseEntity.ok(response);
    }

    //주문 검색
    @GetMapping
    public ResponseEntity<SearchOrderResponseDto> searchOrders(
            @ModelAttribute SearchOrderRequestDto request){
        Pageable pageable = JpaUtil.getNormalPageable(
                request.getPage(), request.getSize(), request.getOrderBy(), request.getSort()
        );
        final SearchOrderResponseDto response = orderService.searchOrders(request, pageable);
        return ResponseEntity.ok(response);
    }

    //주문 취소
    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<OrderStatusResponseDto> cancelOrder(
            @PathVariable UUID orderId) {
        OrderStatusResponseDto response = orderService.cancelOrder(orderId);
        return ResponseEntity.ok(response);
    }

    //주문 완료
    @PatchMapping("{orderId}/completed")
    public void completeOrder(@PathVariable("orderId") UUID orderId) {
        orderService.completeOrder(orderId);
    }

}
