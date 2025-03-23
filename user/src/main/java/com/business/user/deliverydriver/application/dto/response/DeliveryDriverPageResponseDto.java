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
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDriverPageResponseDto {

    private Long deliveryDriverId;
    private UUID hubId;
    private UUID slackId;
    private DriverType driverType;
    private Long deliverySequence;
    private LocalDateTime assignAt;
}
