package com.business.auth.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.business.auth.infrastructure.client.dto.CreateUserRequest;
import com.business.auth.infrastructure.client.dto.UserResponse;

@FeignClient(name = "user-service")
public interface UserClient {

	@PostMapping("/api/v1/users")
	ResponseEntity<Void> createUser(@RequestBody CreateUserRequest request);

	@GetMapping("/api/v1/users/username/{username}")
	ResponseEntity<UserResponse> getUserByUsername(@PathVariable("username") String username);
	
	@GetMapping("/api/v1/users/{userId}")
	ResponseEntity<UserResponse> getUserById(@PathVariable("userId") Long userId);
}
