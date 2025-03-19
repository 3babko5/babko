package com.business.hub.domain.repository;

import com.business.hub.domain.entity.Hub;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HubRepository{
  Hub save(Hub hub);
  boolean existsByHubId(UUID hubId);
  boolean existsByHubName(String hubName);

  Optional<Hub> findById(UUID departureHubId);

  List<Hub> findAll();
}

