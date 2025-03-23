package com.business.hub.domain.repository;

import com.business.hub.application.dto.request.HubSearchRequest;
import com.business.hub.domain.entity.Hub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface HubQueryDslRepository {
    Page<Hub> searchByRequest(HubSearchRequest request, Pageable pageable);
}
