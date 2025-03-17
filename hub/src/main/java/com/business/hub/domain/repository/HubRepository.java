package com.business.hub.domain.repository;

import com.business.hub.domain.entity.Hub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HubRepository extends JpaRepository<Hub, UUID> {
    boolean existsByHubName(String hubName);
}

