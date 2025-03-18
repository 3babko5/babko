package com.business.hub.presentation;



import com.business.common.application.exception.BusinessLogicException;
import com.business.hub.application.dto.request.HubCreateRequest;
import com.business.hub.application.dto.response.HubResponse;
import com.business.hub.application.service.HubService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/hubs")
@RequiredArgsConstructor
public class HubController {

    private final HubService hubService;

    @GetMapping("/health-check")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Health Check OK");
    }



    @PostMapping
    public ResponseEntity<HubResponse> createHub(
            @RequestBody @Valid HubCreateRequest hubCreateRequest
            , Long userId
    ) throws BusinessLogicException {
        userId = 12324L; // 테스트 용 ID
        HubResponse hubResponse = hubService.registerHub(hubCreateRequest, userId);
        return ResponseEntity.ok(hubResponse);
    }



}
