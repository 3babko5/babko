package com.business.delivery.infrastructure.client;



import java.util.UUID;


public interface MockOrderClient {

    void completeOrder(UUID orderId);
}
