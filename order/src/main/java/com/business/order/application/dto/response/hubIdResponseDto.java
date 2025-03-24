package com.business.order.application.dto.response;

import com.business.order.domain.entity.CompanyType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class hubIdResponseDto {
    private List<CompanyData> company;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CompanyData {
        @JsonProperty("companyId")
        private UUID companyId;
        private CompanyType companyType;
        private UUID hubId;

        // supplierId, receiverId 필드는 후처리 메서드로 처리
        public UUID getSupplierId() {
            return companyType == CompanyType.SUPPLIER ? companyId : null;
        }

        public UUID getReceiverId() {
            return companyType == CompanyType.RECEIVER ? companyId : null;
        }
    }
}
