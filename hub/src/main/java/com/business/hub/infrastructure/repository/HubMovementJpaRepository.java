package com.business.hub.infrastructure.repository;

import com.business.hub.application.dto.response.HubMovementResponse;
import com.business.hub.domain.entity.HubMovement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface HubMovementJpaRepository extends JpaRepository<HubMovement, UUID> {
    Optional<HubMovement> findByHubMovementIdAndDeletedAtIsNullAndDeletedByIsNull(UUID hubmovementId);

    Page<HubMovement> findAllByDeletedAtIsNullAndDeletedByIsNull(Pageable pageable);

    List<HubMovement> findByDepartureHub_HubIdAndDeletedByIsNullAndDeletedAtIsNull(UUID depatureHubId);

    List<HubMovement> findByArrivalHub_HubIdAndDeletedByIsNullAndDeletedAtIsNull(UUID arrivalHubId);

    Optional<HubMovement> findByDepartureHub_HubIdAndArrivalHub_HubIdAndDeletedAtIsNullAndDeletedByIsNull(UUID depatureHubId, UUID arrivalHubId);
}
