package com.business.hub.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class HubMovementResponse {

    private UUID departureHubId;
    private UUID arrivalHubId;
    private String departureHubName;
    private String arrivalHubName;
    private BigDecimal distance;
    private int durationTime;

}
