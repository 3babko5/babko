package com.business.auth.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.business.auth.infrastructure.dto.request.UserCreateRequestDto;
import com.business.auth.infrastructure.dto.response.UserCreateResponseDto;

// UserServiceClient: User 서비스와 통신하기 위한 FeignClient
@FeignClient(name = "user-service", url = "http://localhost:8082")
public interface UserServiceClient {
	@PostMapping("/users")
	UserCreateResponseDto createUser(@RequestBody UserCreateRequestDto requestDto);

	@GetMapping("/users/username/{username}")
	UserCreateResponseDto getUserByUsername(@PathVariable("username") String username);

	@GetMapping("/users/{userId}")
	UserCreateResponseDto getUserById(@PathVariable("userId") Long userId);
}