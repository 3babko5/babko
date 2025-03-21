package com.business.delivery.infrastructure.dto.request;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HubRoutesRequestDto {

  private UUID departureHubId;
  private UUID arrivalHubId;
}
