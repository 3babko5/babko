package com.business.user.application.service;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;

import com.business.auth.application.service.AuthService;  // auth-service import
import com.business.user.application.dto.request.UserSignupRequestDto;
import com.business.user.application.dto.response.UserSignupResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
public class UserServiceTest {

	@MockBean
	private AuthService authService; // auth-service를 모킹합니다.

	@Autowired
	private UserService userService; // 실제 userService를 주입받습니다.

	@BeforeEach
	public void setUp() {
		// 모킹된 authService에서 비밀번호를 해싱하는 메서드를 설정합니다.
		when(authService.hashPassword(anyString())).thenReturn("mockedEncryptedPassword");
	}

	@Test
	public void testRegisterUser() {
		// 테스트 데이터 준비
		UserSignupRequestDto requestDto = new UserSignupRequestDto();
		requestDto.setUsername("testUser");
		requestDto.setPassword("plainPassword");
		requestDto.setEmail("test@example.com");
		requestDto.setSlackId("slackTest");
		requestDto.setRole("USER");

		// userService의 registerUser 메서드를 호출합니다.
		UserSignupResponseDto responseDto = userService.registerUser(requestDto);

		// 응답값을 확인합니다.
		assert responseDto.getUsername().equals("testUser");
		assert responseDto.getEmail().equals("test@example.com");
		assert responseDto.getPassword().equals("mockedEncryptedPassword"); // 모킹된 해시된 비밀번호
	}
}
