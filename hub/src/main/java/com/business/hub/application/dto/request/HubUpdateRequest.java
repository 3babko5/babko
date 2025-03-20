package com.business.hub.application.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class HubUpdateRequest {

    private String hubName;
    private String hubAddress;
    private BigDecimal hubLatitude;
    private BigDecimal hubLongitude;
    private Long hubManagerId;

}
