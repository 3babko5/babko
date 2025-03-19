package com.business.hub.infrastructure.repository;

import com.business.hub.domain.entity.HubMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HubMovementJpaRepository extends JpaRepository<HubMovement, UUID> {
}
