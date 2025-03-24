package com.business.user.user.presentation.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.business.common.aop.RoleCheck;
import com.business.user.user.application.dto.request.UserChangeRoleRequest;
import com.business.user.user.application.dto.request.UserSearchRequestDto;
import com.business.user.user.application.dto.request.UserUpdateRequestDto;
import com.business.user.user.application.dto.response.UserDetailResponseDto;
import com.business.user.user.application.dto.response.UserPageResponseDto;
import com.business.user.user.application.service.UserService;
import com.business.user.user.application.dto.request.UserCreateRequestDto;
import com.business.user.user.application.dto.response.UserResponseDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	/**
	 * 회원가입 시 사용자 생성 (Auth 서비스에서 호출)
	 * 누구나 접근 가능 (권한 체크 없음)
	 */
	@PostMapping
	public ResponseEntity<Void> createUser(@Valid @RequestBody UserCreateRequestDto requestDto) {
		userService.createUser(requestDto);
		return ResponseEntity.ok().build();
	}

	/**
	 * 사용자명으로 조회 (Auth 서비스에서 로그인 시 사용)
	 * 누구나 접근 가능 (권한 체크 없음)
	 */
	@GetMapping("/username/{username}")
	public ResponseEntity<UserResponseDto> getUserByUsername(@PathVariable String username) {
		UserResponseDto responseDto = userService.getUserByUsername(username);
		return ResponseEntity.ok(responseDto);
	}

	/**
	 * 사용자 ID로 조회
	 * - 마스터 관리자는 전체 조회 가능
	 * - 그 외 사용자는 자신의 정보만 조회 가능
	 */
	@GetMapping("/{userId}")
	@RoleCheck(roles = {"ROLE_MASTER","ROLE_HUB", "ROLE_DELIVERY", "ROLE_COMPANY"})
	public ResponseEntity<UserDetailResponseDto> getUserById(
		@PathVariable Long userId,
		HttpServletRequest request) {

		Long requesterId = Long.valueOf(request.getHeader("X-client-userId"));
		String requesterRole = request.getHeader("X-client-role");

		UserDetailResponseDto responseDto = userService.getUserByIdWithAccessCheck(userId, requesterId, requesterRole);
		return ResponseEntity.ok(responseDto);
	}

	/**
	 * 사용자 검색 (페이징 + 조건 + 정렬)
	 * - '마스터' 역할만 접근 가능
	 */
	@GetMapping("/search")
	@RoleCheck(roles = {"ROLE_MASTER"})
	public ResponseEntity<Page<UserPageResponseDto>> searchUsers(@ModelAttribute UserSearchRequestDto requestDto) {
		Page<UserPageResponseDto> users = userService.searchUsers(requestDto);
		return ResponseEntity.ok(users);
	}

	/**
	 * 사용자 수정 (비밀번호 제외)
	 * - 마스터만 가능
	 */
	@PutMapping("/{userId}")
	@RoleCheck(roles = {"ROLE_MASTER"})
	public ResponseEntity<Void> updateUser(
		@PathVariable Long userId,
		@Valid @RequestBody UserUpdateRequestDto requestDto,
		HttpServletRequest request) {

		Long updatedBy = Long.valueOf(request.getHeader("X-client-userId"));
		userService.updateUser(userId, requestDto, updatedBy);
		return ResponseEntity.ok().build();
	}

	/**
	 * 사용자 삭제 (Soft Delete)
	 * - 마스터만 가능
	 */
	@DeleteMapping("/{userId}")
	@RoleCheck(roles = {"ROLE_MASTER"})
	public ResponseEntity<Void> deleteUser(@PathVariable Long userId, HttpServletRequest request) {
		Long deletedBy = Long.valueOf(request.getHeader("X-client-userId"));
		userService.deleteUser(userId, deletedBy);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{userId}/role")
	public ResponseEntity<Void> changeUserRole(
		@PathVariable Long userId,
		@RequestBody UserChangeRoleRequest request
	) {
		userService.changeUserRole(userId, request.getNewRole());
		return ResponseEntity.ok().build();
	}
}
