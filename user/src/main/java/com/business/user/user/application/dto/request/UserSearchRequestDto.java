package com.business.user.user.application.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

/**
 * 사용자 검색 요청을 위한 DTO
 * - 키워드 (username or email)
 * - 정렬 기준: createdAt / updatedAt
 * - 페이지 번호, 사이즈 (10, 30, 50만 허용)
 */
@Getter
@Setter
public class UserSearchRequestDto {

	private String keyword = ""; // 검색 키워드

	private String sortBy = "createdAt"; // 정렬 기준

	@Min(0)
	private int page = 0; // 기본 페이지 0

	private int size = 10; // 기본 10건 (10/30/50만 허용)
}
