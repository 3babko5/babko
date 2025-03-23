package com.business.company.domain.entity;

import com.business.common.domain.entity.BaseDataEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "p_companies")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Company extends BaseDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID companyId;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String companyAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CompanyType companyType;

    @Column(nullable = false)
    private UUID hubId;

    @Column(nullable = false)
    private Long companyManagerId;

    @Column(name = "created_by")
    private Long createdBy;

    @Builder
    public Company(String companyName, String companyAddress, CompanyType companyType, UUID hubId, Long companyManagerId, Long createdBy) {
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.companyType = companyType;
        this.hubId = hubId;
        this.companyManagerId = companyManagerId;
        this.createdBy = createdBy;
    }
}