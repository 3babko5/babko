package com.business.company.presentation;

import com.business.common.infrastructure.util.JpaUtil;
import com.business.company.application.dto.request.CreateCompanyRequestDto;
import com.business.company.application.dto.request.SearchCompanyRequestDto;
import com.business.company.application.dto.response.CreateCompanyResponseDto;
import com.business.company.application.dto.response.SearchCompanyResponseDto;
import com.business.company.application.service.CompanyService;
import com.business.company.domain.entity.CompanyType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    public ResponseEntity<CreateCompanyResponseDto> createCompany(@RequestBody CreateCompanyRequestDto createCompanyRequestDto) {
        CreateCompanyResponseDto response = companyService.createCompany(createCompanyRequestDto);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<SearchCompanyResponseDto> searchCompanies(
            @ModelAttribute SearchCompanyRequestDto request) {
        Pageable pageable = JpaUtil.getNormalPageable(
                request.getPage(), request.getSize(), request.getOrderBy(), request.getSort()
        );
        final SearchCompanyResponseDto response = companyService.searchCompanies(request, pageable);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{companyId}")
    public ResponseEntity<Map<String, String>> deleteCompany(@PathVariable UUID companyId) {
        companyService.deleteCompany(companyId);
        return ResponseEntity.ok(Map.of("message", "업체가 삭제되었습니다."));
    }
}