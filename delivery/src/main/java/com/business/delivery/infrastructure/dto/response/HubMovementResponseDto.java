package com.business.delivery.infrastructure.dto.response;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HubMovementResponseDto {

  private UUID departureHubId;
  private UUID arrivalHubId;
  private BigDecimal distance;
  private Long durationTime;
}
