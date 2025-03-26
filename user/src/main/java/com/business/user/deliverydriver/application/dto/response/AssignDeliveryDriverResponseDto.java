package com.business.user.deliverydriver.application.dto.response;

import com.business.user.deliverydriver.domain.entity.DriverType;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignDeliveryDriverResponseDto {

  private Long deliveryDriverId;
  private DriverType driverType;
  private UUID hubId;
  private UUID deliveryRouteId;
  private Long deliverySequence;
  private LocalDateTime assignAt;
}
