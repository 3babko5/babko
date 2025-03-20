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

@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/health-check")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Health Check OK");
    }

    @PostMapping
    public ResponseEntity<CreateCompanyResponseDto> createCompany(@RequestBody CreateCompanyRequestDto createCompanyRequestDto) {
        CreateCompanyResponseDto response = companyService.createCompany(createCompanyRequestDto);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/search")
    public ResponseEntity<SearchCompanyResponseDto> searchCompanies(
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) CompanyType companyType,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "CREATED") String orderBy,
            @RequestParam(defaultValue = "DESC") String sort
    ) {
        final SearchCompanyRequestDto request = new SearchCompanyRequestDto(
                companyName,
                companyType,
                page,
                size,
                orderBy,
                sort
        );
        Pageable pageable = JpaUtil.getNormalPageable(page, size, orderBy, sort);
        final SearchCompanyResponseDto response = companyService.searchCompanies(request, pageable);
        return ResponseEntity.ok(response);
    }
}