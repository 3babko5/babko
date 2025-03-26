package com.business.order.presentaion;

import com.business.common.aop.RoleCheck;
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
    @RoleCheck(roles = {"ROLE_COMPANY", "ROLE_MASTER", "ROLE_HUB","ROLE_DELIVERY"})
    public ResponseEntity<OrderCreateResponseDto> createOrder(
            @Valid @RequestBody OrderCreateRequestDto request,
            @RequestHeader("X-client-userId") Long userId,
            @RequestHeader("X-client-role") String role
    ) {
        OrderCreateResponseDto response = orderService.createOrder(request, userId, role);
        return ResponseEntity.ok(response);
    }

    //주문 단일조회
    @GetMapping("/{orderId}")
    @RoleCheck(roles = {"ROLE_COMPANY", "ROLE_MASTER"})
    public ResponseEntity<OrderGetResponseDto> getOrder(
            @PathVariable UUID orderId,
            @RequestHeader("X-client-userId") Long userId,
            @RequestHeader("X-client-role") String role) {
        OrderGetResponseDto response = orderService.getOrder(orderId, userId, role);
        return ResponseEntity.ok(response);
    }

    //주문 검색
    @GetMapping
    public ResponseEntity<SearchOrderResponseDto> searchOrders(
            @ModelAttribute SearchOrderRequestDto request,
            @RequestHeader("X-client-userId") Long userId,
            @RequestHeader("X-client-role") String role){
        Pageable pageable = JpaUtil.getNormalPageable(
                request.getPage(), request.getSize(), request.getOrderBy(), request.getSort()
        );
        final SearchOrderResponseDto response = orderService.searchOrders(request, pageable, userId, role);
        return ResponseEntity.ok(response);
    }

    //주문 취소
    @PutMapping("/{orderId}/cancel")
    @RoleCheck(roles = {"ROLE_COMPANY", "ROLE_MASTER"})
    public ResponseEntity<OrderStatusResponseDto> cancelOrder(
            @PathVariable UUID orderId,
            @RequestHeader("X-client-userId") Long userId,
            @RequestHeader("X-client-role") String role) {
        OrderStatusResponseDto response = orderService.cancelOrder(orderId, userId, role);
        return ResponseEntity.ok(response);
    }

    //주문 완료
    @PutMapping("{orderId}/completed")
    @RoleCheck(roles = {"ROLE_MASTER"})
    public void completeOrder(@PathVariable("orderId") UUID orderId) {
        orderService.completeOrder(orderId);
    }

}
