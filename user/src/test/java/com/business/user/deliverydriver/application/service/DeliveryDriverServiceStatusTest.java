package com.business.user.deliverydriver.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.business.common.application.exception.BusinessLogicException;
import com.business.user.deliverydriver.application.dto.request.StatusUpdateRequestDto;
import com.business.user.deliverydriver.application.dto.response.DriverStatusUpdateResponseDto;
import com.business.user.deliverydriver.domain.entity.DeliveryDriver;
import com.business.user.deliverydriver.domain.entity.DriverStatus;
import com.business.user.deliverydriver.domain.entity.DriverType;
import com.business.user.deliverydriver.domain.repository.DeliveryDriverRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeliveryDriverServiceStatusTest {

    @Mock
    private DeliveryDriverRepository deliveryDriverRepository;

    @InjectMocks
    private DeliveryDriverService deliveryDriverService;

    private UUID deliveryRouteId;
    private DeliveryDriver driver;
    private final Long userId = 1L;

    @BeforeEach
    void setUp() {
        deliveryRouteId = UUID.randomUUID();

        driver = DeliveryDriver.deliveryDriverCreateBuilder()
            .deliveryDriverId(1L)
            .hubId(UUID.randomUUID())
            .slackId(UUID.randomUUID())
            .driverType(DriverType.HUB)
            .deliverySequence(1L)
            .driverStatus(DriverStatus.ASSIGNED)
            .build();

        driver.assignToRoute(deliveryRouteId, 1L);
    }

    @Test
    void testUpdateDriverStatus_Success() {
        StatusUpdateRequestDto request = StatusUpdateRequestDto.builder()
            .driverStatus(DriverStatus.IN_TRANSIT_TO_HUB)
            .build();

        when(deliveryDriverRepository.findByDeliveryRouteId(deliveryRouteId))
            .thenReturn(Optional.of(driver));
        when(deliveryDriverRepository.save(any(DeliveryDriver.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        DriverStatusUpdateResponseDto responseDto = deliveryDriverService.updateDriverStatus(deliveryRouteId, request, userId);

        assertEquals(DriverStatus.IN_TRANSIT_TO_HUB, responseDto.getDriverStatus());
        assertEquals(driver.getDeliveryDriverId(), responseDto.getDeliveryDriverId());
    }

    @Test
    void testUpdateDriverStatus_InvalidTransition() {
        driver = DeliveryDriver.deliveryDriverCreateBuilder()
            .deliveryDriverId(1L)
            .hubId(UUID.randomUUID())
            .slackId(UUID.randomUUID())
            .driverType(DriverType.HUB)
            .deliverySequence(1L)
            .driverStatus(DriverStatus.DELIVERED)
            .build();
        driver.assignToRoute(deliveryRouteId, 1L);

        StatusUpdateRequestDto request = StatusUpdateRequestDto.builder()
            .driverStatus(DriverStatus.IN_TRANSIT_TO_HUB)
            .build();

        when(deliveryDriverRepository.findByDeliveryRouteId(deliveryRouteId))
            .thenReturn(Optional.of(driver));

        assertThrows(BusinessLogicException.class, () -> {
            deliveryDriverService.updateDriverStatus(deliveryRouteId, request, userId);
        });
    }

    @Test
    void testUpdateDriverStatus_DriverNotFound() {
        StatusUpdateRequestDto request = StatusUpdateRequestDto.builder()
            .driverStatus(DriverStatus.IN_TRANSIT_TO_HUB)
            .build();

        when(deliveryDriverRepository.findByDeliveryRouteId(deliveryRouteId))
            .thenReturn(Optional.empty());

        assertThrows(BusinessLogicException.class, () -> {
            deliveryDriverService.updateDriverStatus(deliveryRouteId, request, userId);
        });
    }
}