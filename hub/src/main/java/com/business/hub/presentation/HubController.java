package com.business.hub.presentation;



import com.business.common.application.exception.BusinessLogicException;
import com.business.hub.application.dto.request.HubCreateRequest;
import com.business.hub.application.dto.request.HubUpdateRequest;
import com.business.hub.application.dto.response.HubPageResponse;
import com.business.hub.application.dto.response.HubResponse;
import com.business.hub.application.service.HubService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @PatchMapping("/{hubId}")
    public ResponseEntity<HubResponse> updateHub(
            @RequestBody @Valid HubUpdateRequest request,
            @PathVariable UUID hubId
            ,Long userId
            ){
        userId =111L;
        HubResponse hubResponse = hubService.updateHub(hubId, request, userId);

        return ResponseEntity.ok(hubResponse);

    }

    @DeleteMapping("/{hubId}")
    public ResponseEntity<HubResponse> deleteHub(
            @PathVariable UUID hubId,
            Long userId
    ){
        userId =111L;
        hubService.deleteHub(hubId, userId);
        return ResponseEntity.noContent().build();
    }

    //허브 단일 조회
    @GetMapping("/{hubId}")
    public ResponseEntity<HubResponse> getHub(@PathVariable UUID hubId)
    {
        HubResponse hubResponse = hubService.getHub(hubId);
        return ResponseEntity.ok(hubResponse);
    }

    //허브 전체 조회
    @GetMapping
    public ResponseEntity<HubPageResponse<HubResponse>> getAllHubs(
            @PageableDefault(size = 10)Pageable pageable)
    {
        HubPageResponse<HubResponse> hubResponse = hubService.getAllHubs(pageable);
        return ResponseEntity.ok(hubResponse);
    }



}
