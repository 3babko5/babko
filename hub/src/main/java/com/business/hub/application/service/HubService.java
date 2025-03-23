package com.business.hub.application.service;



import com.business.common.application.exception.BusinessLogicException;
import com.business.common.infrastructure.api.NaverApiService;
import com.business.hub.application.dto.request.HubCreateRequest;
import com.business.hub.application.dto.request.HubSearchRequest;
import com.business.hub.application.dto.request.HubUpdateRequest;
import com.business.hub.application.dto.response.HubPageResponse;
import com.business.hub.application.dto.response.HubResponse;
import com.business.hub.application.exception.HubExceptionCode;
import com.business.hub.application.mapper.HubMapper;
import com.business.hub.domain.entity.Hub;
import com.business.hub.domain.entity.HubMovement;
import com.business.hub.domain.repository.HubQueryDslRepository;
import com.business.hub.domain.repository.HubRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HubService {

    private final HubRepository hubRepository;
    private final NaverApiService naverApiService;
    private final HubQueryDslRepository hubQueryDslRepository;


    @Transactional
    public HubResponse registerHub(@Valid HubCreateRequest requestDto, Long userId) throws BusinessLogicException {
        LocalDateTime createdAt = LocalDateTime.now();
        double[] coordinates = naverApiService.getCoordinates(requestDto.getHubAddress());

        if (requestDto.getHubName() == null || requestDto.getHubName().trim().isEmpty()) {
            throw new BusinessLogicException(HubExceptionCode.INVALID_HUB_NAME);
        }

        if (requestDto.getHubAddress() == null || requestDto.getHubAddress().trim().isEmpty()) {
            throw new BusinessLogicException(HubExceptionCode.INVALID_HUB_ADDRESS);
        }

        if (hubRepository.existsByHubNameAndHubAddress(requestDto.getHubName(), requestDto.getHubAddress())) {
            throw new BusinessLogicException(HubExceptionCode.DUPLICATE_HUB);
        }

        Hub hub = Hub.builder()
                .createdAt(createdAt)
                .createdBy(userId)
                .hubName(requestDto.getHubName())
                .hubAddress(requestDto.getHubAddress())
                .hubLatitude(BigDecimal.valueOf(coordinates[0]))
                .hubLongitude(BigDecimal.valueOf(coordinates[1]))
                .hubManagerId(requestDto.getHubManagerId())
                .build();

        Hub savedHub = hubRepository.save(hub);

        return HubMapper.toHubResponse(savedHub);
    }


    @Transactional
    public HubResponse updateHub(
            UUID hubId,
            HubUpdateRequest request
            ,Long userId) {
        Hub existingHub = hubRepository.findByHubIdAndDeletedAtIsNullAndDeletedByIsNull(hubId)
                .orElseThrow(() -> new BusinessLogicException(HubExceptionCode.HUB_NOT_FOUND));

        existingHub.update(
                request.getHubName(),
                request.getHubAddress(),
                request.getHubLatitude(),
                request.getHubLongitude(),
                request.getHubManagerId(),
                userId
        );

        hubRepository.save(existingHub);
        return HubMapper.toHubResponse(existingHub);

    }

    public void deleteHub(
            UUID hubId,
            Long userId) {
        Hub existingHub = hubRepository.findByHubIdAndDeletedAtIsNullAndDeletedByIsNull(hubId)
                .orElseThrow(() -> new BusinessLogicException(HubExceptionCode.HUB_NOT_FOUND));

        // 허브 간 이동 정보도 논리 삭제
        for (HubMovement movement : existingHub.getDepartureMovements()) {
            movement.deletedBy(userId);
        }
        for (HubMovement movement : existingHub.getArrivalMovements()) {
            movement.deletedBy(userId);
        }


        existingHub.deletedBy(userId);

        hubRepository.save(existingHub);

    }

    public HubResponse getHub(UUID hubId) {

        Hub hub = hubRepository.findByHubIdAndDeletedAtIsNullAndDeletedByIsNull(hubId)
                .orElseThrow(() -> new BusinessLogicException(HubExceptionCode.HUB_NOT_FOUND));
        return HubMapper.toHubResponse(hub);
    }


    public HubPageResponse<HubResponse> getAllHubs(Pageable pageable) {

        Page<Hub> hubs = hubRepository.findAllByDeletedAtIsNullAndDeletedByIsNull(pageable);

        return HubPageResponse.of(hubs.map(HubMapper::toHubResponse));
    }

    public Page<HubResponse> getHubSearch(
            HubSearchRequest request,
            Pageable pageable) {

        Page<Hub> hubSearch = hubQueryDslRepository.searchByRequest(request, pageable);

        return HubMapper.toPageDto(hubSearch);
    }
}
