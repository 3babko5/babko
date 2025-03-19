package com.business.hub.application.service;

import com.business.hub.application.dto.request.HubMovementCreateRequest;
import com.business.hub.application.dto.response.HubMovementResponse;
import com.business.hub.application.mapper.HubMovementMapper;
import com.business.hub.domain.entity.Hub;
import com.business.hub.domain.entity.HubMovement;
import com.business.hub.domain.repository.HubMovementRepository;
import com.business.hub.domain.repository.HubRepository;
import com.business.hub.infrastructure.client.RouteNaverService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class HubMovementService {

    private final HubMovementRepository hubMovementRepository;
    private final HubRepository hubRepository;
    private final RouteNaverService routeNaverService;  // API 호출 및 캐싱

    @Transactional
    public List<HubMovementResponse> registerHubMovement(@Valid HubMovementCreateRequest request, Long userId) {
        Hub departureHub = hubRepository.findById(request.getDepartureHubId())
                .orElseThrow(() -> new IllegalArgumentException("출발 허브가 존재하지 않습니다."));
        Hub arrivalHub = hubRepository.findById(request.getArrivalHubId())
                .orElseThrow(() -> new IllegalArgumentException("도착 허브가 존재하지 않습니다."));

        List<Hub> hubs = hubRepository.findAll();
        Map<UUID, Hub> hubMap = new HashMap<>();
        for (Hub hub : hubs) {
            hubMap.put(hub.getHubId(), hub);
        }

        List<Hub> optimalPath = findShortestPath(departureHub, arrivalHub, hubMap);

        List<HubMovementResponse> responses = new ArrayList<>();
        for (int i = 0; i < optimalPath.size() - 1; i++) {
            Hub fromHub = optimalPath.get(i);
            Hub toHub = optimalPath.get(i + 1);

            double distance = routeNaverService.getDistance(fromHub, toHub);
            int duration = routeNaverService.getDuration(fromHub, toHub);

            HubMovement hubMovement = HubMovement.builder()
                    .departureHub(fromHub)
                    .arrivalHub(toHub)
                    .distance(BigDecimal.valueOf(distance))
                    .durationTime(duration)
                    .createdBy(userId)
                    .createdAt(LocalDateTime.now())
                    .build();

            hubMovementRepository.save(hubMovement);
            responses.add(HubMovementMapper.toHubMovementResponse(hubMovement));
        }

        return responses;
    }

    private List<Hub> findShortestPath(Hub startHub, Hub endHub, Map<UUID, Hub> hubMap) {
        Map<UUID, Double> distances = new HashMap<>();
        Map<UUID, UUID> previous = new HashMap<>();
        PriorityQueue<UUID> queue = new PriorityQueue<>(Comparator.comparing(distances::get));

        for (UUID hubId : hubMap.keySet()) {
            distances.put(hubId, Double.MAX_VALUE);
        }
        distances.put(startHub.getHubId(), 0.0);
        queue.add(startHub.getHubId());

        while (!queue.isEmpty()) {
            UUID current = queue.poll();
            if (current.equals(endHub.getHubId())) break;

            for (UUID neighborId : hubMap.keySet()) {
                if (!neighborId.equals(current)) {
                    double distance = routeNaverService.getDistance(hubMap.get(current), hubMap.get(neighborId));
                    double newDist = distances.get(current) + distance;
                    if (newDist < distances.get(neighborId)) {
                        distances.put(neighborId, newDist);
                        previous.put(neighborId, current);
                        queue.add(neighborId);
                    }
                }
            }
        }

        List<Hub> path = new ArrayList<>();
        for (UUID at = endHub.getHubId(); at != null; at = previous.get(at)) {
            path.add(hubMap.get(at));
        }
        Collections.reverse(path);
        return path;
    }
}
