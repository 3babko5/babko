package com.business.user.deliverydriver.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.business.common.application.exception.BusinessLogicException;
import com.business.user.deliverydriver.domain.entity.DeliveryDriver;
import com.business.user.deliverydriver.domain.entity.DriverStatus;
import com.business.user.deliverydriver.domain.repository.DeliveryDriverRepository;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeliveryDriverServiceCancelTest {

    @Mock
    private DeliveryDriverRepository deliveryDriverRepository;

    @InjectMocks
    private DeliveryDriverService deliveryDriverService;

    private UUID deliveryRouteId;
    private DeliveryDriver deliveryDriver;

    @BeforeEach
    void setUp() throws Exception {
        deliveryRouteId = UUID.randomUUID();

        deliveryDriver = DeliveryDriver.deliveryDriverCreateBuilder()
            .deliveryDriverId(1L)
            .hubId(UUID.randomUUID())
            .slackId(UUID.randomUUID())
            .driverType(null)
            .deliverySequence(1L)
            .driverStatus(DriverStatus.ASSIGNED)
            .build();

        Field routeField = DeliveryDriver.class.getDeclaredField("deliveryRouteId");
        routeField.setAccessible(true);
        routeField.set(deliveryDriver, deliveryRouteId);
    }

    @Test
    void cancelDriverStatus_Success() {

        when(deliveryDriverRepository.findByDeliveryRouteId(deliveryRouteId))
            .thenReturn(Optional.of(deliveryDriver));

        deliveryDriverService.cancelDriverStatus(deliveryRouteId);

        assertEquals(DriverStatus.CANCELED, deliveryDriver.getDriverStatus());
        verify(deliveryDriverRepository, times(1)).save(deliveryDriver);
    }

    @Test
    void cancelDriverStatus_WhenDriverNotFound_ThrowsException() {

        when(deliveryDriverRepository.findByDeliveryRouteId(deliveryRouteId))
            .thenReturn(Optional.empty());


        assertThrows(BusinessLogicException.class,
            () -> deliveryDriverService.cancelDriverStatus(deliveryRouteId));
    }
}
