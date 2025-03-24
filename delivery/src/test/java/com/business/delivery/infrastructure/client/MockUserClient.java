package com.business.delivery.infrastructure.client;

import java.util.UUID;

public interface MockUserClient {

    void cancelDriverStatus(UUID deliveryRouteId);
}