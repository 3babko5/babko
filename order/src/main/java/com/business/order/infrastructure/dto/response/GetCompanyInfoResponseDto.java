package com.business.order.infrastructure.dto.response;

import com.business.order.domain.entity.CompanyType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class GetCompanyInfoResponseDto {
    private final String message;
    private final int stateCode;
    private final List<CompanyData> company;
    private final PageInfo pageInfo;

    @Getter
    @Builder
    public static class CompanyData {
        private final UUID companyId;
        private final String companyName;
        private final CompanyType companyType;
        private final UUID hubId;
        private final UUID companyManagerId;
    }

    @Getter
    @Builder
    public static class PageInfo {
        private final int page;
        private final int size;
        private final long totalElements;
        private final int totalPages;
        private final boolean isLast;
        private final String sort;
        private final String direction;
    }
}
