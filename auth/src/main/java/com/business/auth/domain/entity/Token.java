package com.business.auth.domain.entity;

import org.hibernate.annotations.Comment;

import com.business.common.domain.entity.BaseDataEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_tokens")
@Comment("토큰 정보")
public class Token extends BaseDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("토큰 ID")
    private Long tokenId;

    @NotNull
    @Column(nullable = false)
    @Comment("사용자 ID")
    private Long userId;

    @NotNull
    @Column(nullable = false, length = 255)
    @Comment("리프레시 토큰")
    private String refreshToken;

    public static Token create(Long userId, String refreshToken) {
        Token token = Token.builder()
            .userId(userId)
            .refreshToken(refreshToken)
            .build();
        
        // 기본적으로 Token 생성자를 userId로 설정
        token.createdBy(userId);
        
        return token;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
} 