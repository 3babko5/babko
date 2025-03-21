package com.business.hub.domain.repository;

import com.business.hub.application.dto.response.HubMovementResponse;
import com.business.hub.domain.entity.HubMovement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface HubMovementRepository{
    void save(HubMovement hubMovement);

    Optional<HubMovement> findByHubMovementIdAndDeletedAtIsNullAndDeletedByIsNull(UUID hubmovementId);

    Page<HubMovement> findAllByDeletedAtIsNullAndDeletedByIsNull(Pageable pageable);

    List<HubMovement> findByDepartureHub_HubIdAndDeletedByIsNullAndDeletedAtIsNull(UUID depatureHubId);

    List<HubMovement> findByArrivalHub_HubIdAndDeletedByIsNullAndDeletedAtIsNull(UUID arrivalHubId);

    Optional<HubMovement> findByDepartureHub_HubIdAndArrivalHub_HubIdAndDeletedAtIsNullAndDeletedByIsNull(UUID depatureHubId, UUID arrivalHubId);
}

