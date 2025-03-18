package com.business.hub.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class HubResponse {
    private UUID hubId;
    private String hubName;
    private String hubAddress;
    private BigDecimal hubLatitude;
    private BigDecimal hubLongitude;
    private UUID hubManagerId;
}
