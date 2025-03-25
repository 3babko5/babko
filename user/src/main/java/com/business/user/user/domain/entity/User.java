package com.business.user.user.domain.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.Comment;

import com.business.common.domain.entity.BaseDataEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "p_users")
@Comment("회원 정보")
public class User extends BaseDataEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Comment("회원 ID")
	private Long userId;

	@NotNull
	@Column(nullable = false, unique = true, length = 100)
	@Comment("사용자명")
	private String username;

	@NotNull
	@Column(nullable = false, length = 255)
	@Comment("비밀번호")
	private String password;

	@NotNull
	@Column(nullable = false, length = 50)
	@Comment("이메일")
	private String email;

	@NotNull
	@Column(nullable = false, unique = true)
	@Comment("슬랙 ID")
	private String slackId;

	@NotNull
	@Column(nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	@Comment("사용자 역할")
	private UserType role;

	public static User create(String username, String password, String email, String slackId, UserType role) {
		return User.builder()

			.username(username)
			.password(password)
			.email(email)
			.slackId(slackId)
			.role(role)
			.build();
	}
	public void updateInfo(String email, String slackId, UserType role) {
		this.email = email;
		this.slackId = slackId;
		this.role = role;
	}
	/**
     * 역할 변경 메서드
     * 새로운 역할로 업데이트
     */
	public void changeRole(UserType newType) {
		this.role = newType;
	}
}

