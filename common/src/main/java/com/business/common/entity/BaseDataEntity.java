package com.business.common.entity;

import com.business.common.util.CommonUtil;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseDataEntity {

    @Column(name = "created_by", nullable = false, updatable = false)
    private UUID createdBy;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_by")
    private UUID updatedBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_by")
    private UUID deletedBy;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public void setCreatedBy(UUID userId) {
        this.createdBy = userId;
        this.createdAt = LocalDateTime.now();
    }

    public void setUpdatedBy(UUID userId) {
        this.updatedBy = userId;
        this.updatedAt = LocalDateTime.now();
    }

    public void setDeletedBy(UUID userId) {
        this.deletedBy = userId;
        this.deletedAt = LocalDateTime.now();
    }

    public void initBaseDataFromToken(UUID userId) {
        String nowAsString = CommonUtil.LDTToString(LocalDateTime.now());
        this.createdBy = userId;
        this.createdAt = CommonUtil.stringToLDT(nowAsString);
        this.updatedBy = userId;
        this.updatedAt = CommonUtil.stringToLDT(nowAsString);
    }

}
