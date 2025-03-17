package com.business.delivery.application.dto.request;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDeliveryRouteRequestDto {

  private UUID deliveryId;
  private UUID startHubId;
  private UUID endHubId;
}