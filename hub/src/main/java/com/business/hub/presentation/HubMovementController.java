package com.business.hub.presentation;

import com.business.hub.application.dto.request.HubMovementCreateRequest;
import com.business.hub.application.dto.request.HubMovementUpdateRequest;
import com.business.hub.application.dto.response.HubMovementPageResponse;
import com.business.hub.application.dto.response.HubMovementResponse;
import com.business.hub.application.service.HubMovementService;
import com.business.hub.domain.entity.HubMovement;
import com.business.hub.domain.repository.HubMovementRepository;
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
    public ResponseEntity<List<HubMovementResponse>> registerHubMovement(
            @RequestBody @Valid HubMovementCreateRequest request,
            Long userId) {

        userId = 1111L;
        List<HubMovementResponse> responseList = hubMovementService.registerHubMovement(request, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseList);
    }

    //단건 조회
    @GetMapping("/{hubmovementId}")
    public ResponseEntity<HubMovementResponse> getHubMovement(
            @PathVariable UUID hubMovementId) {
        HubMovementResponse response = hubMovementService.getHubMovement(hubMovementId);

        return ResponseEntity.ok(response);
    }

    //전체 조회
    @GetMapping
    public ResponseEntity<HubMovementPageResponse<HubMovementResponse>> getAllHubMovements(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        HubMovementPageResponse<HubMovementResponse> response = hubMovementService.getAllHubMovements(pageable);

        return ResponseEntity.ok(response);

    }

    //출발 허브 기준 이동 정보 조회
    @GetMapping("/departure/{departureHubId}")
    public ResponseEntity<List<HubMovementResponse>>getMovementsByDepartureHub(
            @PathVariable UUID departureHubId
    ){
        List<HubMovementResponse> responseList = hubMovementService.getMovementsByDepartureHub(departureHubId);
        return ResponseEntity.ok(responseList);
    }

    //도착 허브 기준 이동 정보 조회
    @GetMapping("/arrival/{arrivalHubId}")
    public ResponseEntity<List<HubMovementResponse>> getMovementsByArrivalHub(
            @PathVariable UUID arrivalHubId
    ){
        List<HubMovementResponse> responseList = hubMovementService.getMovementsByArrivalHub(arrivalHubId);
        return ResponseEntity.ok(responseList);
    }

    //특정 허브 간 이동 경로 조회 api
    @GetMapping("/path")
    public ResponseEntity<HubMovementResponse> getMovementsByHubs(
            @RequestParam UUID departureHubId,
            @RequestParam UUID arrivalHubId
    ){
        HubMovementResponse response = hubMovementService.getMovementsByHubs(departureHubId,arrivalHubId);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{hubMovementId}")
    public ResponseEntity<HubMovementResponse> updateHubMovement(
            @PathVariable UUID hubMovementId,
            @RequestBody @Valid HubMovementUpdateRequest request,
            Long userId
    ){
        userId = 1112L;
        HubMovementResponse response = hubMovementService.updateHubMovement(hubMovementId, request, userId);
        return ResponseEntity.ok(response);
    }

}
