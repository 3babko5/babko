package com.business.user.deliverydriver.application.dto.request;

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
public class DeliveryDriverSearchRequestDto {

    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private Integer size = 10;

    @Builder.Default
    private String orderBy = "createdAt";

    @Builder.Default
    private String sort = "desc";

    private Long deliveryDriverId;
    private UUID hubId;
    private UUID slackId;
    private DriverType driverType;
    private Long deliverySequence;
    private LocalDateTime assignAt;
}
