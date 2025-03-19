package com.business.order.presentaion;

import com.business.order.application.dto.request.OrderCreateRequestDto;
import com.business.order.application.dto.response.OrderCreateResponseDto;
import com.business.order.application.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/health-check")
    ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Health Check OK");
    }

    @PostMapping
    public ResponseEntity<OrderCreateResponseDto> createOrder(
            @Valid @RequestBody OrderCreateRequestDto request,
            Long userId
    ) {
        userId = 1L;
        OrderCreateResponseDto response = orderService.createOrder(request, userId);
        return ResponseEntity.ok(response);
    }
}
