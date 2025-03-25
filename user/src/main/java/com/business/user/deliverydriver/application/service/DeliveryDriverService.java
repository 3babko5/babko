package com.business.user.deliverydriver.application.service;

import com.business.common.application.exception.BusinessLogicException;
import com.business.user.deliverydriver.application.dto.mapper.DeliveryDriverResponseMapper;
import com.business.user.deliverydriver.application.dto.mapper.DeliveryDriverRequestMapper;
import com.business.user.deliverydriver.application.dto.request.CreateDeliveryDriverRequestDto;
import com.business.user.deliverydriver.application.dto.request.DeliveryDriverSearchRequestDto;
import com.business.user.deliverydriver.application.dto.request.StatusUpdateRequestDto;
import com.business.user.deliverydriver.application.dto.response.AssignDeliveryDriverResponseDto;
import com.business.user.deliverydriver.application.dto.response.DeliveryDriverDetailResponseDto;
import com.business.user.deliverydriver.application.dto.response.DeliveryDriverResponseDto;
import com.business.user.deliverydriver.application.dto.response.DriverStatusUpdateResponseDto;
import com.business.user.deliverydriver.application.exception.DeliveryDriverErrorCode;
import com.business.user.deliverydriver.domain.entity.DeliveryDriver;
import com.business.user.deliverydriver.domain.entity.DriverStatus;
import com.business.user.deliverydriver.domain.entity.DriverType;
import com.business.user.deliverydriver.domain.repository.DeliveryDriverRepository;
import com.business.user.deliverydriver.infrastructure.client.DeliveryClient;
import com.business.user.deliverydriver.infrastructure.client.HubClient;
import com.business.user.deliverydriver.infrastructure.dto.response.DeliveryClientResponseDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryDriverService {

    private final DeliveryDriverRepository deliveryDriverRepository;
    private final DeliveryClient deliveryClient;

    public DeliveryDriverResponseDto createDeliveryDriver(CreateDeliveryDriverRequestDto request, Long userId, String role) {

        if (userId == null) {
            throw new BusinessLogicException(DeliveryDriverErrorCode.INVALID_USER_ID);
        }

        if (deliveryDriverRepository.existsById(request.getDeliveryDriverId())) {
            throw new BusinessLogicException(DeliveryDriverErrorCode.DRIVER_ALREADY_EXISTS);
        }

        if (request.getDriverType() == DriverType.HUB && request.getHubId() != null) {
            throw new BusinessLogicException(DeliveryDriverErrorCode.INVALID_DRIVER_TYPE);
        }

        if (request.getDriverType() == DriverType.COMPANY) {
            long companyDriverCount = deliveryDriverRepository.countByHubIdAndDriverType(request.getHubId(), DriverType.COMPANY);
            if (companyDriverCount >= 10) {
                throw new BusinessLogicException(DeliveryDriverErrorCode.MAX_COMPANY_DRIVERS_PER_HUB_EXCEEDED);
            }
        }

        if (request.getDriverType() == DriverType.HUB) {
            long hubDriverCount = deliveryDriverRepository.countByDriverType(DriverType.HUB);
            if (hubDriverCount >= 10) {
                throw new BusinessLogicException(DeliveryDriverErrorCode.MAX_HUB_DRIVERS_EXCEEDED);
            }
        }

        if (request.getDriverType() == DriverType.COMPANY) {
            long companyDriverCount = deliveryDriverRepository.countByHubIdAndDriverType(request.getHubId(), DriverType.COMPANY);
            if (companyDriverCount >= 10) {
                throw new BusinessLogicException(DeliveryDriverErrorCode.MAX_COMPANY_DRIVERS_PER_HUB_EXCEEDED);
            }
        }

        Long newSequence = switch (request.getDriverType()) {
            case HUB -> deliveryDriverRepository.findLastDeliverySequenceForHubDrivers().orElse(0L) + 1;
            case COMPANY -> deliveryDriverRepository.findLastDeliverySequenceForCompanyDrivers(request.getHubId()).orElse(0L) + 1;
        };

        DeliveryDriver deliveryDriver =
            DeliveryDriverRequestMapper.createRequestToEntity(request, newSequence);

        deliveryDriverRepository.save(deliveryDriver);

        return DeliveryDriverResponseMapper.driverToDriverResponseDto(deliveryDriver);
    }

    public List<AssignDeliveryDriverResponseDto> assignDriversForDelivery(UUID deliveryId, Long userId, String role) {

        if (userId == null) {
            throw new BusinessLogicException(DeliveryDriverErrorCode.INVALID_USER_ID);
        }

        List<DeliveryClientResponseDto> routes = deliveryClient.getRoutesByDeliveryId(deliveryId, userId, role);

        List<AssignDeliveryDriverResponseDto> assignedDrivers = new ArrayList<>();

        for (DeliveryClientResponseDto route : routes) {
            Long assignedDriverId;

            if (isLastDestination(route)) {
                assignedDriverId = assignNextDeliveryDriver(DriverType.COMPANY, route.getDestinationHubId());
            } else {
                assignedDriverId = assignNextDeliveryDriver(DriverType.HUB, route.getOriginHubId());
            }

            DeliveryDriver assignedDriver = saveAssignedDriver(assignedDriverId, route.getDeliveryRouteId(), route.getRouteSequence());
            assignedDrivers.add(DeliveryDriverResponseMapper.driverToAssignedDto(assignedDriver));
        }
        return assignedDrivers;
    }

    private DeliveryDriver saveAssignedDriver(Long deliveryDriverId, UUID deliveryRouteId, Long routeSequence) {

        DeliveryDriver driver = deliveryDriverRepository.findByDeliveryDriverId(deliveryDriverId)
            .orElseThrow(() -> new BusinessLogicException(DeliveryDriverErrorCode.NO_AVAILABLE_DRIVER));

        driver.assignToRoute(deliveryRouteId, routeSequence);

        return deliveryDriverRepository.save(driver);
    }

    private Long assignNextDeliveryDriver(DriverType driverType, UUID hubId) {

        Optional<DeliveryDriver> lastAssignedDriver =
            driverType == DriverType.COMPANY
                ? deliveryDriverRepository.findLastAssignedDriverByTypeAndHub(driverType, hubId)
                : deliveryDriverRepository.findLastAssignedDriver();

        if (lastAssignedDriver.isPresent()) {
            Long currentSequence = lastAssignedDriver.get().getDeliverySequence();

            Optional<DeliveryDriver> nextDriver =
                driverType == DriverType.COMPANY
                    ? deliveryDriverRepository.findNextAvailableDriverByTypeAndHub(currentSequence, driverType, hubId)
                    : deliveryDriverRepository.findNextAvailableDriver(currentSequence);

            if (nextDriver.isPresent()) {
                return updateAssignedDriver(nextDriver.get()).getDeliveryDriverId();
            }
        }

        return deliveryDriverRepository.findFirstAvailableDriver()
            .map(this::updateAssignedDriver)
            .map(DeliveryDriver::getDeliveryDriverId)
            .orElseThrow(() -> new BusinessLogicException(DeliveryDriverErrorCode.NO_AVAILABLE_DRIVER));
    }

    private DeliveryDriver updateAssignedDriver(DeliveryDriver driver) {

        driver.updateAssignAt(LocalDateTime.now());
        return driver;
    }

    private boolean isLastDestination(DeliveryClientResponseDto route) {

        return route.getDeliveryAddress() != null;
    }

    @Transactional(readOnly = true)
    public Page<DeliveryDriverResponseDto> getDrivers(DeliveryDriverSearchRequestDto request, Long userId, String role) {

        if (userId == null) {
            throw new BusinessLogicException(DeliveryDriverErrorCode.INVALID_USER_ID);
        }



        Pageable pageable = DeliveryDriverRequestMapper.SearchRequestDtoToPageable(request);

        Page<DeliveryDriver> driverPage = deliveryDriverRepository.findDeliveryDrivers(request, pageable);
        return driverPage.map(DeliveryDriverResponseMapper::driverToDriverResponseDto);
    }

    @Transactional(readOnly = true)
    public DeliveryDriverDetailResponseDto getDriverByDriverId(Long deliveryDriverId, Long userId, String role) {

        if (userId == null) {
            throw new BusinessLogicException(DeliveryDriverErrorCode.INVALID_USER_ID);
        }

        DeliveryDriver deliveryDriver = deliveryDriverRepository.findByDeliveryDriverId(deliveryDriverId)
            .orElseThrow(() -> new BusinessLogicException(DeliveryDriverErrorCode.DELIVERY_DRIVER_NOT_FOUND));

        UUID deliveryRouteId = deliveryDriver.getDeliveryRouteId();
        if (deliveryRouteId == null) {
            throw new BusinessLogicException(DeliveryDriverErrorCode.DELIVERY_ROUTE_NOT_ASSIGNED);
        }

        List<DeliveryClientResponseDto> deliveryRoutes = safeGetRoutesByDeliveryId(deliveryRouteId, userId, role);
        if (deliveryRoutes.isEmpty()) {
            throw new BusinessLogicException(DeliveryDriverErrorCode.EXTERNAL_DELIVERY_ROUTE_NOT_FOUND);
        }

        DeliveryClientResponseDto assignedRoute = deliveryRoutes.stream()
            .filter(route -> route.getRouteSequence().equals(deliveryDriver.getRouteSequence()))
            .findFirst()
            .orElseThrow(() -> new BusinessLogicException(DeliveryDriverErrorCode.EXTERNAL_DELIVERY_ROUTE_NOT_FOUND));

        return DeliveryDriverResponseMapper.driverToDriverDetailDto(deliveryDriver, assignedRoute);
    }

    private List<DeliveryClientResponseDto> safeGetRoutesByDeliveryId(UUID deliveryRouteId, Long userId, String role) {
        try {
            List<DeliveryClientResponseDto> routes = deliveryClient.getRoutesByDeliveryId(deliveryRouteId, userId, role);
            return routes == null ? Collections.emptyList() : routes;
        } catch (Exception e) {
            throw new BusinessLogicException(DeliveryDriverErrorCode.EXTERNAL_DELIVERY_ROUTE_NOT_FOUND);
        }
    }

    @Transactional
    public DriverStatusUpdateResponseDto updateDriverStatus(UUID deliveryRouteId, StatusUpdateRequestDto request, Long userId, String role) {

        if (userId == null) {
            throw new BusinessLogicException(DeliveryDriverErrorCode.INVALID_USER_ID);
        }

        DeliveryDriver driver = deliveryDriverRepository.findByDeliveryRouteId(deliveryRouteId)
            .orElseThrow(() -> new BusinessLogicException(DeliveryDriverErrorCode.DELIVERY_DRIVER_NOT_FOUND));

        DriverStatus driverStatus = request.getDriverStatus();
        driver.updateStatus(driverStatus);

        DeliveryDriver updatedDriver = deliveryDriverRepository.save(driver);

        return DeliveryDriverResponseMapper.toStatusUpdateResponse(updatedDriver);
    }

    @Transactional
    public void cancelDriverStatus(UUID deliveryRouteId, Long userId, String role) {

        if (userId == null) {
            throw new BusinessLogicException(DeliveryDriverErrorCode.INVALID_USER_ID);
        }

        DeliveryDriver driver = deliveryDriverRepository.findByDeliveryRouteId(deliveryRouteId)
            .orElseThrow(() -> new BusinessLogicException(DeliveryDriverErrorCode.DRIVER_CANCEL_ERROR));

        driver.updateCancelStatus();

        deliveryDriverRepository.save(driver);
    }
}