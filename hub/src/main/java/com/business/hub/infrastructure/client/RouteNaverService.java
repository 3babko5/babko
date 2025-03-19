package com.business.hub.infrastructure.client;

import com.business.common.infrastructure.api.NaverApiService;
import com.business.hub.domain.entity.Hub;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RouteNaverService {

    private final NaverApiService naverApiService;
    private final Map<String, Double> distanceCache = new HashMap<>();
    private final Map<String, Integer> durationCache = new HashMap<>();

    public double getDistance(Hub fromHub, Hub toHub) {
        String key = fromHub.getHubId().toString() + "-" + toHub.getHubId().toString();
        return distanceCache.computeIfAbsent(key, k -> naverApiService.extractDistanceFromJson(naverApiService.getOptimalRoute(
                fromHub.getHubLatitude().doubleValue(), fromHub.getHubLongitude().doubleValue(),
                toHub.getHubLatitude().doubleValue(), toHub.getHubLongitude().doubleValue()
        )).doubleValue());
    }

    public int getDuration(Hub fromHub, Hub toHub) {
        String key = fromHub.getHubId().toString() + "-" + toHub.getHubId().toString();
        return durationCache.computeIfAbsent(key, k -> naverApiService.extractDurationFromJson(naverApiService.getOptimalRoute(
                fromHub.getHubLatitude().doubleValue(), fromHub.getHubLongitude().doubleValue(),
                toHub.getHubLatitude().doubleValue(), toHub.getHubLongitude().doubleValue()
        )));
    }
}
