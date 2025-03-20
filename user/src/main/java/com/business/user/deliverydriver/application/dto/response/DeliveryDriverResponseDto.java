package com.business.user.deliverydriver.application.dto.response;

import com.business.user.deliverydriver.domain.entity.DriverType;
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
