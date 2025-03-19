package com.business.user.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.business.user.application.dto.request.UserSignupRequestDto;
import com.business.user.application.dto.response.UserSigninResponseDto;
import com.business.user.application.dto.response.UserSignupResponseDto;
import com.business.user.application.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

	private final UserService userService;

	/**
	 * 회원가입
	 * 201 Created
	 */
	@PostMapping("/register")
	public ResponseEntity<UserSignupResponseDto> registerUser(@RequestBody @Valid UserSignupRequestDto request) {
		UserSignupResponseDto response = userService.registerUser(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	/**
	 * 유저 정보 조회
	 * 200 OK
	 */
	@GetMapping("/{username}")
	public ResponseEntity<UserSigninResponseDto> getUser(@PathVariable String username) {
		UserSigninResponseDto response = userService.getUserByUsername(username);
		return ResponseEntity.ok(response);
	}
}
