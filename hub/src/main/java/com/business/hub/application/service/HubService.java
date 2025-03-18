package com.business.hub.application.service;



import com.business.common.infrastructure.api.NaverApiService;
import com.business.hub.application.dto.request.HubCreateRequest;
import com.business.hub.application.dto.response.HubResponse;
import com.business.hub.application.mapper.HubMapper;
import com.business.hub.domain.entity.Hub;
import com.business.hub.domain.repository.HubRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class HubService {

    private final HubRepository hubRepository;
    private final NaverApiService naverApiService;


    @Transactional
    public HubResponse registerHub(@Valid HubCreateRequest requestDto, Long userId) {

        LocalDateTime createdAt = LocalDateTime.now();
        double[] coordinates = naverApiService.getCoordinates(requestDto.getHubAddress());

        Hub hub = Hub.builder()
                .createdBy(userId)
                .createdAt(createdAt)
                .hubName(requestDto.getHubName())
                .hubAddress(requestDto.getHubAddress())
                .hubLatitude(BigDecimal.valueOf(coordinates[0]))  // 위도
                .hubLongitude(BigDecimal.valueOf(coordinates[1])) // 경도
                .hubManagerId(requestDto.getHubManagerId())
                .build();

        Hub savedHub = hubRepository.save(hub);

        return HubMapper.toHubResponse(savedHub);
    }
}
