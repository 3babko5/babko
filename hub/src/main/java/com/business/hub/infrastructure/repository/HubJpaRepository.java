package com.business.hub.infrastructure.repository;

import com.business.hub.application.dto.request.HubSearchRequest;
import com.business.hub.domain.entity.Hub;
import com.business.hub.domain.repository.HubQueryDslRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface HubJpaRepository extends JpaRepository<Hub, UUID> {

    boolean existsByHubId(UUID hubId);

    boolean existsByHubName(String hubName);

    boolean existsByHubNameAndHubAddress(String hubName, String hubAddress);

    Optional<Hub> findById(UUID hubId);


    Optional<Hub> findByHubIdAndDeletedAtIsNullAndDeletedByIsNull(UUID hubId);

    Page<Hub> findAllByDeletedAtIsNullAndDeletedByIsNull(Pageable pageable);

}
