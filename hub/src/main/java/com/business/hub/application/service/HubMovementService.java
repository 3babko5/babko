package com.business.hub.application.service;

import com.business.common.application.exception.BusinessLogicException;
import com.business.hub.application.dto.request.HubMovementCreateRequest;
import com.business.hub.application.dto.request.HubMovementUpdateRequest;
import com.business.hub.application.dto.response.HubMovementPageResponse;
import com.business.hub.application.dto.response.HubMovementResponse;
import com.business.hub.application.exception.HubExceptionCode;
import com.business.hub.application.mapper.HubMovementMapper;
import com.business.hub.domain.entity.Hub;
import com.business.hub.domain.entity.HubMovement;
import com.business.hub.domain.repository.HubMovementRepository;
import com.business.hub.domain.repository.HubRepository;
import com.business.hub.infrastructure.client.RouteNaverService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HubMovementService {

    private final HubMovementRepository hubMovementRepository;
    private final HubRepository hubRepository;
    private final RouteNaverService routeNaverService;

    @Transactional
    public List<HubMovementResponse> registerHubMovement(@Valid HubMovementCreateRequest request, Long userId) {
        Hub departureHub = hubRepository.findByHubIdAndDeletedAtIsNullAndDeletedByIsNull(request.getDepartureHubId())
                .orElseThrow(() -> new BusinessLogicException(HubExceptionCode.HUB_NOT_FOUND));
        Hub arrivalHub = hubRepository.findByHubIdAndDeletedAtIsNullAndDeletedByIsNull(request.getArrivalHubId())
                .orElseThrow(() -> new BusinessLogicException(HubExceptionCode.HUB_NOT_FOUND));

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

    @Transactional
    public HubMovementResponse getHubMovement(UUID hubmovementId) {
        HubMovement hubMoveMent = hubMovementRepository.findByHubMovementIdAndDeletedAtIsNullAndDeletedByIsNull(hubmovementId)
                .orElseThrow(() -> new BusinessLogicException(HubExceptionCode.Hub_MOVEMENT_NOT_FOUND));

        return HubMovementMapper.toHubMovementResponse(hubMoveMent);
    }

    @Transactional
    public HubMovementPageResponse<HubMovementResponse> getAllHubMovements(Pageable pageable) {
        Page<HubMovement> hubMovement = hubMovementRepository.findAllByDeletedAtIsNullAndDeletedByIsNull(pageable);

        return HubMovementPageResponse.of(hubMovement.map(HubMovementMapper::toHubMovementResponse));
    }

    @Transactional
    public List<HubMovementResponse> getMovementsByDepartureHub(UUID depatureHubId) {
        List<HubMovement> departureHubMovement = hubMovementRepository
                .findByDepartureHub_HubIdAndDeletedByIsNullAndDeletedAtIsNull(depatureHubId);

        if (departureHubMovement.isEmpty()) {
            throw new BusinessLogicException(HubExceptionCode.Hub_MOVEMENT_NOT_FOUND);
        }

        return departureHubMovement.stream().map(HubMovementMapper::toHubMovementResponse).toList();
    }

    @Transactional
    public List<HubMovementResponse> getMovementsByArrivalHub(UUID arrivalHubId) {
        List<HubMovement> arrivalHubMovement = hubMovementRepository
                .findByArrivalHub_HubIdAndDeletedByIsNullAndDeletedAtIsNull(arrivalHubId);

        if (arrivalHubMovement.isEmpty()) {
            throw new BusinessLogicException(HubExceptionCode.Hub_MOVEMENT_NOT_FOUND);
        }

        return arrivalHubMovement.stream().map(HubMovementMapper::toHubMovementResponse).toList();

    }

    @Transactional
    public HubMovementResponse getMovementsByHubs(
            UUID depatureHubId,
            UUID arrivalHubId) {
        HubMovement hubMovement = hubMovementRepository
                .findByDepartureHub_HubIdAndArrivalHub_HubIdAndDeletedAtIsNullAndDeletedByIsNull(depatureHubId, arrivalHubId)
                .orElseThrow(() -> new BusinessLogicException(HubExceptionCode.Hub_MOVEMENT_NOT_FOUND));

        return HubMovementMapper.toHubMovementResponse(hubMovement);
    }

    @Transactional
    public HubMovementResponse updateHubMovement(
            UUID hubMovementId,
            @Valid HubMovementUpdateRequest request,
            Long userId) {
        HubMovement hubMovement = hubMovementRepository
                .findByHubMovementIdAndDeletedAtIsNullAndDeletedByIsNull(hubMovementId)
                .orElseThrow(() -> new BusinessLogicException(HubExceptionCode.Hub_MOVEMENT_NOT_FOUND));

        Hub departureHub = hubRepository.findById(request.getDepartureHubId())
                .orElseThrow(() -> new BusinessLogicException(HubExceptionCode.HUB_NOT_FOUND));
        Hub arrivalHub = hubRepository.findById(request.getArrivalHubId())
                .orElseThrow(() -> new BusinessLogicException(HubExceptionCode.HUB_NOT_FOUND));

        BigDecimal newDistance = BigDecimal.valueOf(routeNaverService.getDistance(departureHub, arrivalHub));
        int newDurationTime = routeNaverService.getDuration(departureHub, arrivalHub);

        hubMovement.update(departureHub, arrivalHub, newDistance, newDurationTime, userId);
        hubMovementRepository.save(hubMovement);

        return HubMovementMapper.toHubMovementResponse(hubMovement);
    }

    @Transactional
    public void deleteHubMovement(
            UUID hubMovementId,
            Long userId) {

        HubMovement hubMovement = hubMovementRepository
                .findByHubMovementIdAndDeletedAtIsNullAndDeletedByIsNull(hubMovementId)
                .orElseThrow(() -> new BusinessLogicException(HubExceptionCode.Hub_MOVEMENT_NOT_FOUND));

        hubMovement.deletedBy(userId);
        hubMovementRepository.save(hubMovement);
    }

    public List<HubMovementResponse> getRoutes(
            UUID departureHubId,
            UUID arrivalHubId) {

        Hub departureHub = hubRepository.findById(departureHubId)
                .orElseThrow(() -> new BusinessLogicException(HubExceptionCode.HUB_NOT_FOUND));

        Hub arrivalHub = hubRepository.findById(arrivalHubId)
                .orElseThrow(() -> new BusinessLogicException(HubExceptionCode.HUB_NOT_FOUND));

        List<Hub> allHubs = hubRepository.findAll();
        Map<UUID, Hub> hubMap = allHubs.stream().collect(Collectors.toMap(Hub::getHubId, h -> h));

        List<Hub> shortestPath = findShortestPath(departureHub, arrivalHub, hubMap);

        List<HubMovementResponse> result = new ArrayList<>();

        for (int i = 0; i < shortestPath.size() - 1; i++) {
            UUID fromHub = shortestPath.get(i).getHubId();
            UUID toHub = shortestPath.get(i + 1).getHubId();

            HubMovement movement = hubMovementRepository
                    .findByDepartureHub_HubIdAndArrivalHub_HubIdAndDeletedAtIsNullAndDeletedByIsNull(fromHub, toHub)
                    .orElseThrow(() -> new BusinessLogicException(HubExceptionCode.Hub_MOVEMENT_NOT_FOUND));

            result.add(HubMovementMapper.toHubMovementResponse(movement));
        }

        return result;
    }
}
