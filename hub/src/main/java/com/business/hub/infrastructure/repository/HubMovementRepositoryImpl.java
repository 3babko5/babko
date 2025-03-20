package com.business.hub.infrastructure.repository;

import com.business.hub.application.dto.response.HubMovementResponse;
import com.business.hub.domain.entity.HubMovement;
import com.business.hub.domain.repository.HubMovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class HubMovementRepositoryImpl implements HubMovementRepository {

    private final HubMovementJpaRepository hubMovementJpaRepository;

    @Override
    public void save(HubMovement hubMovement) {
        hubMovementJpaRepository.save(hubMovement);
    }

    @Override
    public Optional<HubMovement> findByHubMovementIdAndDeletedAtIsNullAndDeletedByIsNull(UUID hubmovementId) {
        return hubMovementJpaRepository.findByHubMovementIdAndDeletedAtIsNullAndDeletedByIsNull(hubmovementId);
    }

    @Override
    public Page<HubMovement> findAllByDeletedAtIsNullAndDeletedByIsNull(Pageable pageable) {
        return hubMovementJpaRepository.findAllByDeletedAtIsNullAndDeletedByIsNull(pageable);
    }

    @Override
    public List<HubMovement> findByDepartureHub_HubIdAndDeletedByIsNullAndDeletedAtIsNull(UUID departureHubId) {
        return hubMovementJpaRepository.findByDepartureHub_HubIdAndDeletedByIsNullAndDeletedAtIsNull(departureHubId);
    }

    @Override
    public List<HubMovement> findByArrivalHub_HubIdAndDeletedByIsNullAndDeletedAtIsNull(UUID arrivalHubId) {
        return hubMovementJpaRepository.findByArrivalHub_HubIdAndDeletedByIsNullAndDeletedAtIsNull(arrivalHubId);
    }

    @Override
    public Optional<HubMovement> findByDepartureHub_HubIdAndArrivalHub_HubIdAndDeletedAtIsNullAndDeletedByIsNull(UUID depatureHubId, UUID arrivalHubId) {
        return hubMovementJpaRepository.findByDepartureHub_HubIdAndArrivalHub_HubIdAndDeletedAtIsNullAndDeletedByIsNull(depatureHubId, arrivalHubId);
    }
}
