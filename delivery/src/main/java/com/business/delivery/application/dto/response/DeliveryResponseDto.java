package com.business.delivery.application.dto.response;

import com.business.delivery.domain.entity.DeliveryStatus;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryResponseDto {

  private UUID deliveryId;
  private UUID orderId;
  private DeliveryStatus deliveryStatus;
}
