package com.business.hub.presentation;

import com.business.common.aop.RoleCheck;
import com.business.hub.application.dto.request.HubMovementCreateRequest;
import com.business.hub.application.dto.request.HubMovementUpdateRequest;
import com.business.hub.application.dto.response.HubMovementPageResponse;
import com.business.hub.application.dto.response.HubMovementResponse;
import com.business.hub.application.service.HubMovementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/hub-movements")
@RequiredArgsConstructor
public class HubMovementController {

    private final HubMovementService hubMovementService;

    @PostMapping
    @RoleCheck(roles = {"ROLE_MASTER"})
    public ResponseEntity<List<HubMovementResponse>> registerHubMovement(
            @RequestBody @Valid HubMovementCreateRequest request,
            @RequestHeader("X-client-userId") Long userId) {

        List<HubMovementResponse> responseList = hubMovementService.registerHubMovement(request, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseList);
    }


    @GetMapping("/{hubMovementId}")
    @RoleCheck(roles = {"ROLE_MASTER", "ROLE_HUB", "ROLE_DELIVERY", "ROLE_COMPANY"})

    public ResponseEntity<HubMovementResponse> getHubMovement(
            @PathVariable UUID hubMovementId) {
        HubMovementResponse response = hubMovementService.getHubMovement(hubMovementId);

        return ResponseEntity.ok(response);
    }


    @GetMapping
    @RoleCheck(roles = {"ROLE_MASTER", "ROLE_HUB", "ROLE_DELIVERY", "ROLE_COMPANY"})
    public ResponseEntity<HubMovementPageResponse<HubMovementResponse>> getAllHubMovements(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        HubMovementPageResponse<HubMovementResponse> response = hubMovementService.getAllHubMovements(pageable);

        return ResponseEntity.ok(response);

    }


    @GetMapping("/departure/{departureHubId}")
    @RoleCheck(roles = {"ROLE_MASTER", "ROLE_HUB", "ROLE_DELIVERY", "ROLE_COMPANY"})
    public ResponseEntity<List<HubMovementResponse>>getMovementsByDepartureHub(
            @PathVariable UUID departureHubId
    ){
        List<HubMovementResponse> responseList = hubMovementService.getMovementsByDepartureHub(departureHubId);
        return ResponseEntity.ok(responseList);
    }


    @GetMapping("/arrival/{arrivalHubId}")
    @RoleCheck(roles = {"ROLE_MASTER", "ROLE_HUB", "ROLE_DELIVERY", "ROLE_COMPANY"})
    public ResponseEntity<List<HubMovementResponse>> getMovementsByArrivalHub(
            @PathVariable UUID arrivalHubId
    ){
        List<HubMovementResponse> responseList = hubMovementService.getMovementsByArrivalHub(arrivalHubId);
        return ResponseEntity.ok(responseList);
    }


    @GetMapping("/path")
    @RoleCheck(roles = {"ROLE_MASTER", "ROLE_HUB", "ROLE_DELIVERY", "ROLE_COMPANY"})
    public ResponseEntity<HubMovementResponse> getMovementsByHubs(
            @RequestParam UUID departureHubId,
            @RequestParam UUID arrivalHubId
    ){
        HubMovementResponse response = hubMovementService.getMovementsByHubs(departureHubId,arrivalHubId);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{hubMovementId}")
    @RoleCheck(roles = {"ROLE_MASTER"})
    public ResponseEntity<HubMovementResponse> updateHubMovement(
            @PathVariable UUID hubMovementId,
            @RequestBody @Valid HubMovementUpdateRequest request,
            @RequestHeader("X-client-userId") Long userId
    ){

        HubMovementResponse response = hubMovementService.updateHubMovement(hubMovementId, request, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{hubMovementId}")
    @RoleCheck(roles = {"ROLE_MASTER"})
    public ResponseEntity<HubMovementResponse> deleteHubMovement(
            @PathVariable UUID hubMovementId,
            @RequestHeader("X-client-userId") Long userId
    ){

        hubMovementService.deleteHubMovement(hubMovementId, userId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/routes")
    @RoleCheck(roles = {"ROLE_MASTER", "ROLE_HUB", "ROLE_DELIVERY", "ROLE_COMPANY"})
    public ResponseEntity<List<HubMovementResponse>> getRoutes(
            @RequestParam UUID departureHubId,
            @RequestParam UUID arrivalHubId
    ) {
        List<HubMovementResponse> route = hubMovementService.getRoutes(departureHubId, arrivalHubId);
        return ResponseEntity.ok(route);
    }


}
