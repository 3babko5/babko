package com.business.hub.domain.repository;

import com.business.hub.domain.entity.HubMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HubMovementRepository extends JpaRepository<HubMovement, UUID> {
}

