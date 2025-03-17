package com.business.delivery.infrastructure.client;

import com.business.delivery.infrastructure.dto.response.HubMovementResponseDto;
import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "hub-service", url = "http://localhost:8086/api/v1/hub-movements")
public interface HubMovementClient {

  @GetMapping("/routes")
  List<HubMovementResponseDto> getRoutesByStartAndEndHub(

      @RequestParam("departureHubId") UUID startHubId,
      @RequestParam("arrivalHubId") UUID endHubId
  );
}
