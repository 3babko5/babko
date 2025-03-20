package com.business.auth.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.business.auth.application.dto.request.AuthSignupRequestDto;
import com.business.auth.application.dto.response.AuthSigninResponseDto;
import com.business.auth.application.dto.response.AuthSignupResponseDto;

@FeignClient(name = "user-service", url = "http://user-service:8084/api/v1/users")
public interface UserClient {

	@PostMapping("/register")
	AuthSignupResponseDto registerUser(@RequestBody AuthSignupRequestDto request);

	@GetMapping("/{user_id}") // 사용자 조회 요청
	AuthUserSigninResponseDto getUserByUserId(@PathVariable("user_id") String userId);
}
