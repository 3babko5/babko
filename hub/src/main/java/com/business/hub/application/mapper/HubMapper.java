package com.business.hub.application.mapper;

import com.business.common.application.exception.BusinessLogicException;
import com.business.hub.application.dto.response.HubResponse;
import com.business.hub.application.exception.HubExceptionCode;
import com.business.hub.domain.entity.Hub;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class HubMapper {

    public static HubResponse toHubResponse(Hub hub) {
        return HubResponse.builder()
                .hubId(hub.getHubId())
                .hubName(hub.getHubName())
                .hubAddress(hub.getHubAddress())
                .hubLatitude(hub.getHubLatitude())
                .hubLongitude(hub.getHubLongitude())
                .hubManagerId(hub.getHubManagerId())
                .build();
    }

    public static List<HubResponse> toDto(List<Hub> hubs) {
        if (hubs == null || hubs.isEmpty()) {
            throw new BusinessLogicException(HubExceptionCode.HUB_NOT_FOUND);
        }

        return hubs.stream()
                .map(HubMapper::toHubResponse)
                .collect(Collectors.toList());
    }

    public static Page<HubResponse> toPageDto(Page<Hub> hubs) {
        if (hubs == null || hubs.isEmpty()) {
            throw new BusinessLogicException(HubExceptionCode.HUB_NOT_FOUND);
        }

        return hubs.map(HubMapper::toHubResponse);
    }

}
