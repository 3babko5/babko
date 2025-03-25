package com.business.delivery.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.business.common.application.exception.BusinessLogicException;
import com.business.delivery.application.dto.request.StatusUpdateRequestDto;
import com.business.delivery.application.dto.response.DeliveryStatusUpdateResponseDto;
import com.business.delivery.domain.entity.Delivery;
import com.business.delivery.domain.entity.DeliveryRoute;
import com.business.delivery.domain.entity.DeliveryRouteStatus;
import com.business.delivery.domain.entity.DeliveryStatus;
import com.business.delivery.domain.repository.DeliveryRepository;
import com.business.delivery.infrastructure.client.OrderClient;
import com.business.delivery.infrastructure.client.UserClient;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceStatusTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private UserClient mockUserClient;

    @Mock
    private OrderClient mockOrderClient;

    @InjectMocks
    private DeliveryService deliveryService;

    private Delivery delivery;
    private DeliveryRoute activeRoute;
    private UUID deliveryId;
    private final Long userId = 1L;
    private final String role = "ROLE_HUB";

    @BeforeEach
    void setUp() throws Exception {
        deliveryId = UUID.randomUUID();
        UUID routeId = UUID.randomUUID();

        delivery = Delivery.deliveryCreateBuilder()
            .orderId(UUID.randomUUID())
            .startHubId(UUID.randomUUID())
            .endHubId(UUID.randomUUID())
            .deliveryAddress("서울시 강남구")
            .recipientId(UUID.randomUUID())
            .build();

        activeRoute = DeliveryRoute.deliveryRouteCreateBuilder()
            .delivery(delivery)
            .routeSequence(1L)
            .originHubId(UUID.randomUUID())
            .destinationHubId(UUID.randomUUID())
            .estimatedDistance(BigDecimal.valueOf(15.5))
            .estimatedTime(30L)
            .deliveryRouteStatus(DeliveryRouteStatus.PENDING)
            .deliveryAddress("서울 -> 대전")
            .build();

        delivery.addDeliveryRoute(List.of(activeRoute));
    }

    @Test
    void testUpdateDeliveryStatus_Success() {

        StatusUpdateRequestDto request = StatusUpdateRequestDto.builder()
            .deliveryStatus(DeliveryStatus.OUT_FOR_DELIVERY)
            .deliveryRouteStatus(DeliveryRouteStatus.IN_TRANSIT_TO_HUB)
            .build();

        when(deliveryRepository.findByDeliveryId(deliveryId)).thenReturn(delivery);
        when(deliveryRepository.save(any(Delivery.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DeliveryStatusUpdateResponseDto responseDto =
            deliveryService.updateDeliveryStatus(deliveryId, request, userId, role);


        assertEquals(DeliveryStatus.OUT_FOR_DELIVERY, responseDto.getDeliveryStatus());
        assertEquals(DeliveryRouteStatus.IN_TRANSIT_TO_HUB, responseDto.getDeliveryRouteStatus());

        verify(mockUserClient, times(1)).updateDriverStatus(activeRoute.getDeliveryRouteId(), userId, role);
    }

    @Test
    void testUpdateDeliveryStatus_DeliveryNotFound() {

        when(deliveryRepository.findByDeliveryId(deliveryId)).thenReturn(null);

        StatusUpdateRequestDto request = StatusUpdateRequestDto.builder()
            .deliveryStatus(DeliveryStatus.OUT_FOR_DELIVERY)
            .deliveryRouteStatus(DeliveryRouteStatus.IN_TRANSIT_TO_HUB)
            .build();

        assertThrows(BusinessLogicException.class, () -> {
            deliveryService.updateDeliveryStatus(deliveryId, request, userId, role);

        });
    }

    @Test
    void testUpdateDeliveryStatus_RouteNotFound() {

        delivery.getDeliveryRoutes().clear();
        when(deliveryRepository.findByDeliveryId(deliveryId)).thenReturn(delivery);

        StatusUpdateRequestDto request = StatusUpdateRequestDto.builder()
            .deliveryStatus(DeliveryStatus.OUT_FOR_DELIVERY)
            .deliveryRouteStatus(DeliveryRouteStatus.IN_TRANSIT_TO_HUB)
            .build();

        assertThrows(BusinessLogicException.class, () -> {
            deliveryService.updateDeliveryStatus(deliveryId, request, userId, role);

        });
    }

    @Test
    void testUpdateDeliveryStatus_FinalRouteComplete() {

        delivery = Delivery.deliveryCreateBuilder()
            .orderId(UUID.randomUUID())
            .startHubId(UUID.randomUUID())
            .endHubId(UUID.randomUUID())
            .deliveryAddress("서울시 강남구")
            .recipientId(UUID.randomUUID())
            .build();

        DeliveryRoute route1 = DeliveryRoute.deliveryRouteCreateBuilder()
            .delivery(delivery)
            .routeSequence(1L)
            .originHubId(UUID.randomUUID())
            .destinationHubId(UUID.randomUUID())
            .estimatedDistance(BigDecimal.valueOf(10))
            .estimatedTime(20L)
            .deliveryRouteStatus(DeliveryRouteStatus.PENDING)
            .deliveryAddress("서울 -> 대전")
            .build();

        DeliveryRoute route2 = DeliveryRoute.deliveryRouteCreateBuilder()
            .delivery(delivery)
            .routeSequence(2L)
            .originHubId(UUID.randomUUID())
            .destinationHubId(UUID.randomUUID())
            .estimatedDistance(BigDecimal.valueOf(20))
            .estimatedTime(30L)
            .deliveryRouteStatus(DeliveryRouteStatus.DELIVERED)
            .deliveryAddress("대전 -> 부산")
            .build();

        delivery.getDeliveryRoutes().clear();
        delivery.addDeliveryRoute(List.of(route1, route2));

        StatusUpdateRequestDto request = StatusUpdateRequestDto.builder()
            .deliveryStatus(DeliveryStatus.OUT_FOR_DELIVERY)
            .deliveryRouteStatus(DeliveryRouteStatus.IN_TRANSIT_TO_HUB)
            .build();

        when(deliveryRepository.findByDeliveryId(deliveryId)).thenReturn(delivery);
        when(deliveryRepository.save(any(Delivery.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DeliveryStatusUpdateResponseDto responseDto =
            deliveryService.updateDeliveryStatus(deliveryId, request, userId, role);

        assertEquals(DeliveryStatus.DELIVERED, responseDto.getDeliveryStatus());
        assertEquals(DeliveryRouteStatus.IN_TRANSIT_TO_HUB, responseDto.getDeliveryRouteStatus());

        verify(mockUserClient, times(1)).updateDriverStatus(route1.getDeliveryRouteId(), userId, role);
    }

    @Test
    void testUpdateDeliveryStatus_ExternalDriverStatusUpdateCalled() {

        delivery = Delivery.deliveryCreateBuilder()
            .orderId(UUID.randomUUID())
            .startHubId(UUID.randomUUID())
            .endHubId(UUID.randomUUID())
            .deliveryAddress("서울시 강남구")
            .recipientId(UUID.randomUUID())
            .build();

        activeRoute = DeliveryRoute.deliveryRouteCreateBuilder()
            .delivery(delivery)
            .routeSequence(1L)
            .originHubId(UUID.randomUUID())
            .destinationHubId(UUID.randomUUID())
            .estimatedDistance(BigDecimal.valueOf(15.5))
            .estimatedTime(30L)
            .deliveryRouteStatus(DeliveryRouteStatus.PENDING)
            .deliveryAddress("서울 -> 대전")
            .build();

        delivery.getDeliveryRoutes().clear();
        delivery.addDeliveryRoute(List.of(activeRoute));

        StatusUpdateRequestDto request = StatusUpdateRequestDto.builder()
            .deliveryStatus(DeliveryStatus.OUT_FOR_DELIVERY)
            .deliveryRouteStatus(DeliveryRouteStatus.IN_TRANSIT_TO_HUB)
            .build();

        when(deliveryRepository.findByDeliveryId(deliveryId)).thenReturn(delivery);
        when(deliveryRepository.save(any(Delivery.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DeliveryStatusUpdateResponseDto responseDto =
            deliveryService.updateDeliveryStatus(deliveryId, request, userId, role);


        verify(mockUserClient, times(1)).updateDriverStatus(activeRoute.getDeliveryRouteId(), userId, role);
    }

    @Test
    void testUpdateDeliveryStatus_LastRouteNotDelivered_NoCompleteOrderCall() {

        StatusUpdateRequestDto request = StatusUpdateRequestDto.builder()
            .deliveryStatus(DeliveryStatus.OUT_FOR_DELIVERY)
            .deliveryRouteStatus(DeliveryRouteStatus.ARRIVED_AT_HUB)
            .build();

        when(deliveryRepository.findByDeliveryId(deliveryId)).thenReturn(delivery);
        when(deliveryRepository.save(any(Delivery.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DeliveryStatusUpdateResponseDto responseDto =
            deliveryService.updateDeliveryStatus(deliveryId, request, userId, role);


        assertEquals(DeliveryStatus.OUT_FOR_DELIVERY, responseDto.getDeliveryStatus());
        verify(mockOrderClient, never())
            .completeOrder(any(UUID.class), eq(userId), eq(role));
    }
}
