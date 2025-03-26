package com.business.user.deliverydriver.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "hub-service")
public interface HubClient {

  @GetMapping("/api/v1/hubs")
  boolean existsByHubId(
      @RequestParam("hubId") String hubId,
      @RequestHeader("X-client-userId") Long userId,
      @RequestHeader("X-client-role") String role
  );
}
