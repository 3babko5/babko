package com.business.hub.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HubMovementUpdateRequest {

    private UUID departureHubId;

    private UUID arrivalHubId;

    private BigDecimal distance;

    private int durationTime;
}

