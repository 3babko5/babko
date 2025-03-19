package com.business.user.deliverydriver.application.dto.request;

import com.business.user.deliverydriver.domain.entity.DriverType;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDeliveryDriverRequestDto {

  private Long deliveryDriverId;
  private UUID hubId;
  private UUID slackId;
  private DriverType driverType;
}
