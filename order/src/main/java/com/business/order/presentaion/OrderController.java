package com.business.order.presentaion;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @GetMapping("/health-check")
    ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Health Check OK");
    }
}
