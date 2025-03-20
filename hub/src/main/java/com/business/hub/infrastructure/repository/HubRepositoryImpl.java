package com.business.hub.infrastructure.repository;

import com.business.hub.domain.entity.Hub;
import com.business.hub.domain.repository.HubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class HubRepositoryImpl implements HubRepository {

    private final HubJpaRepository hubJpaRepository;

    @Override
    public Hub save(Hub hub) {
        return hubJpaRepository.save(hub);
    }

    @Override
    public boolean existsByHubId(UUID hubId) {
        return hubJpaRepository.existsByHubId(hubId);
    }

    @Override
    public boolean existsByHubName(String hubName) {
        return hubJpaRepository.existsByHubName(hubName);
    }

    @Override
    public Optional<Hub> findById(UUID departureHubId) {
        return hubJpaRepository.findById(departureHubId);
    }

    @Override
    public List<Hub> findAll() {
        return hubJpaRepository.findAll();
    }

    @Override
    public boolean existsByHubNameAndHubAddress(String hubName, String hubAddress) {
        return hubJpaRepository.existsByHubNameAndHubAddress(hubName, hubAddress);
    }

    @Override
    public Optional<Hub> findByHubIdAndDeletedAtIsNullAndDeletedByIsNull(UUID hubId){
        return hubJpaRepository.findByHubIdAndDeletedAtIsNullAndDeletedByIsNull(hubId);
    }

    @Override
    public Page<Hub> findAllByDeletedAtIsNullAndDeletedByIsNull(Pageable pageable) {
        return hubJpaRepository.findAllByDeletedAtIsNullAndDeletedByIsNull(pageable);
    }
}
