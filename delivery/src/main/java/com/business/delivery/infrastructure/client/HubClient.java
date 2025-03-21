package com.business.delivery.infrastructure.client;

import com.business.delivery.infrastructure.dto.response.HubIdResponseDto;
import com.business.delivery.infrastructure.dto.response.HubRoutesResponseDto;
import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "hub-service", url = "${hub.service.url}")
public interface HubClient {

  @GetMapping("/api/v1/hub-movements/routes")
  List<HubRoutesResponseDto> getRoutesByStartAndEndHub(
      @RequestParam("departureHubId") UUID startHubId,
      @RequestParam("arrivalHubId") UUID endHubId
  );

  @GetMapping("/api/v1/hubs/{hub_id}")
  HubIdResponseDto getLatitudeAndLongitude(
      @PathVariable("hub_id") UUID endHubId
  );
}

