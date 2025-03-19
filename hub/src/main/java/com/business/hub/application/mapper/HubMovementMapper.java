package com.business.hub.application.mapper;

import com.business.hub.application.dto.response.HubMovementResponse;
import com.business.hub.domain.entity.HubMovement;

import java.util.List;
import java.util.stream.Collectors;

public class HubMovementMapper {

    public static HubMovementResponse toHubMovementResponse(HubMovement hubMovement) {
        return HubMovementResponse.builder()
                .departureHubId(hubMovement.getDepartureHub().getHubId())
                .arrivalHubId(hubMovement.getArrivalHub().getHubId())
                .distance(hubMovement.getDistance())
                .departureHubName(hubMovement.getDepartureHub().getHubName())
                .arrivalHubName(hubMovement.getArrivalHub().getHubName())
                .durationTime(hubMovement.getDurationTime())
                .build();
    }

    public static List<HubMovementResponse> toDtoList(List<HubMovement> hubMovements) {
        if (hubMovements == null || hubMovements.isEmpty()) {
            return List.of();
        }
        return hubMovements.stream()
                .map(HubMovementMapper::toHubMovementResponse)
                .collect(Collectors.toList());
    }
}
