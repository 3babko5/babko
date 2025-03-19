package com.business.company.presentation;

import com.business.company.application.dto.request.CreateCompanyRequestDto;
import com.business.company.application.dto.response.CreateCompanyResponseDto;
import com.business.company.application.service.CompanyService;
import lombok.RequiredArgsConstructor;
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
}
