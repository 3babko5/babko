package com.business.user.user.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.business.common.aop.RoleCheck;
import com.business.user.user.application.service.UserService;
import com.business.user.user.domain.dto.CreateUserRequestDto;
import com.business.user.user.domain.dto.UserResponseDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping
	public ResponseEntity<Void> createUser(@Valid @RequestBody CreateUserRequestDto requestDto) {
		userService.createUser(requestDto);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{userId}")
	@RoleCheck(roles = {"ROLE_MASTER", "ROLE_COMPANY"})
	public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId) {
		UserResponseDto responseDto = userService.getUserById(userId);
		return ResponseEntity.ok(responseDto);
	}

	@GetMapping("/username/{username}")
	public ResponseEntity<UserResponseDto> getUserByUsername(@PathVariable String username) {
		UserResponseDto responseDto = userService.getUserByUsername(username);
		return ResponseEntity.ok(responseDto);
	}
}
