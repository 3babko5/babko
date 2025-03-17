package com.business.delivery.presentation.controller;

import com.business.delivery.application.dto.request.CreateDeliveryRouteRequestDto;
import com.business.delivery.application.dto.response.DeliveryRouteResponseDto;
import com.business.delivery.application.service.DeliveryRouteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/delivery-routes")
public class DeliveryRouteController {

  /**
   * 문제점
   * 1. 다른 micro-service가 배송 생성 기능을 구현할 만큼 구현되지 않았음
   * 2. 따라서 실제 연관된 마이크로 서비스와 통신 할 수 없음
   * 3. 다른 마이크로서비스가 개발이 완료되어 통신이 가능하다고 하더라도 모든 마이크로 서비스를 올려야 하기 때문에
   * 4. 배송 담당자는 배송 관련 API 테스트를 위해 연관된 모든 도메인에 데이터를 생성해야함
   *
   * 해결
   * MSA
   */

  private final DeliveryRouteService deliveryRouteService;

  //    @PreAuthorize("hasRole('MASTER')")
  @PostMapping
  public ResponseEntity<DeliveryRouteResponseDto> createDeliveryRoute(
      @Valid @RequestBody CreateDeliveryRouteRequestDto request
//      @AuthenticationPrincipal CustomUserDetails principal
  ) {
    DeliveryRouteResponseDto response = deliveryRouteService.createDeliveryRoute(request);
    return ResponseEntity.ok(response);
  }
}
