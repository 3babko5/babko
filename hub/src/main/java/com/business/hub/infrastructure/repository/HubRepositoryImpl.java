package com.business.hub.infrastructure.repository;

import com.business.hub.domain.entity.Hub;
import com.business.hub.domain.repository.HubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class HubRepositoryImpl implements HubRepository {

    private final HubRepository hubRepository;

    @Override
    public Hub save(Hub hub) {
        return hubRepository.save(hub);
    }

    @Override
    public boolean existsByHubId(UUID hubId) {
        return hubRepository.existsByHubId(hubId);
    }

    @Override
    public boolean existsByHubName(String hubName) {
        return hubRepository.existsByHubName(hubName);
    }

    @Override
    public Optional<Hub> findById(UUID departureHubId) {
        return Optional.empty();
    }

    @Override
    public List<Hub> findAll() {
        return hubRepository.findAll();
    }
}
