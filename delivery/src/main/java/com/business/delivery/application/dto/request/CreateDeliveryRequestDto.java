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
public class CreateDeliveryRequestDto {

  private UUID orderId;
  private UUID startHubId;
  private UUID endHubId;
  private String deliveryAddress;
  private UUID recipientId;
}
