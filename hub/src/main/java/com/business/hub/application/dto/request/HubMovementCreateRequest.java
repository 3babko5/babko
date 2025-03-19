package com.business.hub.application.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;
@Getter
@Builder
public class HubMovementCreateRequest {

    private UUID departureHubId;
    private UUID arrivalHubId;

}
