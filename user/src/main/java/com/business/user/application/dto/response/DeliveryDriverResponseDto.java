package com.business.user.application.dto.response;

import com.business.user.domain.entity.DriverType;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryDriverResponseDto {

  private Long deliveryDriverId;
  private UUID hubId;
  private UUID slackId;
  private DriverType driverType;
  private Long deliverySequence;
}
