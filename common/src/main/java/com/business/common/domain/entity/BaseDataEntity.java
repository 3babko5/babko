package com.business.common.domain.entity;

import com.business.common.infrastructure.util.CommonUtil;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseDataEntity {

    @Column(name = "created_by", nullable = false, updatable = false)
    private Long createdBy;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_by")
    private Long deletedBy;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public void setCreatedBy(Long userId) {
        this.createdBy = userId;
        this.createdAt = LocalDateTime.now();
    }

    public void setUpdatedBy(Long userId) {
        this.updatedBy = userId;
        this.updatedAt = LocalDateTime.now();
    }

    public void setDeletedBy(Long userId) {
        this.deletedBy = userId;
        this.deletedAt = LocalDateTime.now();
    }

    public void initBaseDataFromToken(Long userId) {
        String nowAsString = CommonUtil.LDTToString(LocalDateTime.now());
        this.createdBy = userId;
        this.createdAt = CommonUtil.stringToLDT(nowAsString);
        this.updatedBy = userId;
        this.updatedAt = CommonUtil.stringToLDT(nowAsString);
    }

}
