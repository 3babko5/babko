package com.business.hub.infrastructure.repository;

import com.business.hub.domain.entity.HubMovement;
import com.business.hub.domain.repository.HubMovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HubMovementRepositoryImpl implements HubMovementRepository {

    private final HubMovementJpaRepository hubMovementJpaRepository;

    @Override
    public void save(HubMovement hubMovement) {
        hubMovementJpaRepository.save(hubMovement);
    }
}
