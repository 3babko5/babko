package com.business.hub.domain.repository;

import com.business.hub.domain.entity.HubMovement;
import org.springframework.stereotype.Repository;


@Repository

public interface HubMovementRepository{
    void save(HubMovement hubMovement);
}

