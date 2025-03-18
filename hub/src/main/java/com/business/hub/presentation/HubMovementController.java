package com.business.hub.presentation;

import com.business.hub.application.dto.request.HubMovementCreateRequest;
import com.business.hub.application.dto.response.HubMovementResponse;
import com.business.hub.application.service.HubMovementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

}
