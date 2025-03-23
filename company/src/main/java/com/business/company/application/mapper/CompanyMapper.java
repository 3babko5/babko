package com.business.company.application.mapper;

import com.business.company.application.dto.request.CreateCompanyRequestDto;
import com.business.company.application.dto.response.CreateCompanyResponseDto;
import com.business.company.application.dto.response.SearchCompanyResponseDto;
import com.business.company.domain.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

public class CompanyMapper {

    public static Company toEntity(CreateCompanyRequestDto dto) {
        return Company.builder()
                .companyName(dto.getCompanyName())
                .companyAddress(dto.getCompanyAddress())
                .companyType(dto.getCompanyType())
                .hubId(dto.getHubId())
                .companyManagerId(dto.getCompanyManagerId())
                .createdBy(1L)
                .build();
    }

    public static CreateCompanyResponseDto toResponseDto(Company company) {
        return CreateCompanyResponseDto.builder()
                .message("업체가 등록되었습니다.")
                .stateCode(201)
                .company(CreateCompanyResponseDto.CompanyData.builder()
                        .companyId(company.getCompanyId())
                        .companyName(company.getCompanyName())
                        .companyType(company.getCompanyType())
                        .hubId(company.getHubId())
                        .companyManagerId(company.getCompanyManagerId())
                        .build())
                .build();
    }

    public static SearchCompanyResponseDto toSearchResponseDto(Page<Company> companyPage) {
        List<SearchCompanyResponseDto.CompanyData> companyDataList = companyPage.getContent().stream()
                .map(company -> SearchCompanyResponseDto.CompanyData.builder()
                        .companyId(company.getCompanyId())
                        .companyName(company.getCompanyName())
                        .companyType(company.getCompanyType())
                        .hubId(company.getHubId())
                        .companyManagerId(company.getCompanyManagerId())
                        .build())
                .collect(Collectors.toList());

        SearchCompanyResponseDto.PageInfo pageInfo = SearchCompanyResponseDto.PageInfo.builder()
                .page(companyPage.getNumber())
                .size(companyPage.getSize())
                .totalElements(companyPage.getTotalElements())
                .totalPages(companyPage.getTotalPages())
                .isLast(companyPage.isLast())
                .sort(companyPage.getSort().toString())
                .direction(companyPage.getSort().stream().findFirst().map(Sort.Order::getDirection).orElse(Sort.Direction.DESC).name())
                .build();

        return SearchCompanyResponseDto.builder()
                .message("조회 완료하였습니다.")
                .stateCode(200)
                .company(companyDataList)
                .pageInfo(pageInfo)
                .build();
    }
}