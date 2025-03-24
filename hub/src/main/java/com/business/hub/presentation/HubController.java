package com.business.hub.presentation;



import com.business.common.aop.RoleCheck;
import com.business.common.application.exception.BusinessLogicException;
import com.business.hub.application.dto.request.HubCreateRequest;
import com.business.hub.application.dto.request.HubSearchRequest;
import com.business.hub.application.dto.request.HubUpdateRequest;
import com.business.hub.application.dto.response.HubPageResponse;
import com.business.hub.application.dto.response.HubResponse;
import com.business.hub.application.service.HubService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/hubs")
@RequiredArgsConstructor
public class HubController {

    private final HubService hubService;

    @PostMapping
    @RoleCheck(roles = {"ROLE_MASTER"})
    public ResponseEntity<HubResponse> createHub(
            @RequestBody @Valid HubCreateRequest hubCreateRequest
            ,@RequestHeader("X-client-userId") Long userId
    ) throws BusinessLogicException {

        HubResponse hubResponse = hubService.registerHub(hubCreateRequest, userId);
        return ResponseEntity.ok(hubResponse);
    }


    @PatchMapping("/{hubId}")
    @RoleCheck(roles = {"ROLE_MASTER"})
    public ResponseEntity<HubResponse> updateHub(
            @RequestBody @Valid HubUpdateRequest request,
            @PathVariable UUID hubId,
            @RequestHeader("X-client-userId") Long userId
            ){

        HubResponse hubResponse = hubService.updateHub(hubId, request, userId);

        return ResponseEntity.ok(hubResponse);

    }

    @DeleteMapping("/{hubId}")
    @RoleCheck(roles = {"ROLE_MASTER"})
    public ResponseEntity<HubResponse> deleteHub(
            @PathVariable UUID hubId,
            @RequestHeader("X-client-userId") Long userId
    ){

        hubService.deleteHub(hubId, userId);
        return ResponseEntity.noContent().build();
    }

    //허브 단일 조회
    @GetMapping("/{hubId}")
    @RoleCheck(roles = {"ROLE_MASTER", "ROLE_HUB", "ROLE_DELIVERY", "ROLE_COMPANY"})
    public ResponseEntity<HubResponse> getHub(@PathVariable UUID hubId)
    {
        HubResponse hubResponse = hubService.getHub(hubId);
        return ResponseEntity.ok(hubResponse);
    }

    //허브 전체 조회
    @GetMapping
    @RoleCheck(roles = {"ROLE_MASTER", "ROLE_HUB", "ROLE_DELIVERY", "ROLE_COMPANY"})
    public ResponseEntity<HubPageResponse<HubResponse>> getAllHubs(
            @PageableDefault(size = 10)Pageable pageable)
    {
        HubPageResponse<HubResponse> hubResponse = hubService.getAllHubs(pageable);
        return ResponseEntity.ok(hubResponse);
    }


    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.initDirectFieldAccess();
    }

    // 허브 검색
    @GetMapping("/search")
    @RoleCheck(roles = {"ROLE_MASTER", "ROLE_HUB", "ROLE_DELIVERY", "ROLE_COMPANY"})
    public ResponseEntity<Page<HubResponse>> getHubSearch(
            @ModelAttribute HubSearchRequest request,
            @PageableDefault(sort = "hubName", direction = Sort.Direction.ASC) Pageable pageable
    ){

        Page<HubResponse> hubResponses = hubService.getHubSearch(request, pageable);

        return ResponseEntity.ok(hubResponses);
    }

}
