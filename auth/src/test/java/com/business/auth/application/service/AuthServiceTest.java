// package com.business.auth.application.service;
//
// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;
//
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.springframework.security.crypto.password.PasswordEncoder;
//
// import com.business.auth.application.dto.request.AuthLoginRequestDto;
// import com.business.auth.application.dto.request.AuthRefreshRequestDto;
// import com.business.auth.application.dto.request.AuthRegisterRequestDto;
// import com.business.auth.application.dto.response.AuthLoginResponseDto;
// import com.business.auth.application.dto.response.AuthTokenResponseDto;
// import com.business.auth.domain.entity.UserType;
// import com.business.auth.infrastructure.client.UserServiceClient;
// import com.business.auth.infrastructure.config.JwtTokenProvider;
// import com.business.auth.infrastructure.dto.request.UserCreateRequestDto;
// import com.business.auth.infrastructure.dto.response.UserCreateResponseDto;
//
// public class AuthServiceTest {
//
// 	// UserServiceClient를 Mock 객체로 생성 (외부 서비스 호출을 모킹)
// 	@Mock
// 	private UserServiceClient userServiceClient;
//
// 	// PasswordEncoder를 Mock 객체로 생성 (비밀번호 암호화 및 검증 모킹)
// 	@Mock
// 	private PasswordEncoder passwordEncoder;
//
// 	// JwtTokenProvider를 Mock 객체로 생성 (JWT 생성 및 검증 모킹)
// 	@Mock
// 	private JwtTokenProvider jwtTokenProvider;
//
// 	// AuthService를 테스트 대상(Mock 객체를 주입받음)
// 	@InjectMocks
// 	private AuthService authService;
//
// 	@BeforeEach
// 	public void setUp() {
// 		// Mockito 어노테이션을 초기화하여 Mock 객체 활성화
// 		MockitoAnnotations.openMocks(this);
// 	}
//
// 	@Test
// 	public void testRegister_Success() {
// 		// 회원가입 성공 테스트
// 		// Given - 테스트에 필요한 데이터 설정
// 		AuthRegisterRequestDto requestDto = new AuthRegisterRequestDto("testuser", "password123", "test@example.com",
// 			"slack123", UserType.ROLE_COMPANY);
//
// 		// 비밀번호를 암호화된 값으로 변환하는 동작을 모킹
// 		when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");
//
// 		// UserServiceClient를 통해 회원 생성 요청을 보낼 때 예상 응답을 설정
// 		UserCreateResponseDto dummyUser = new UserCreateResponseDto(1L, "testuser", "hashedPassword",
// 			"test@example.com", "ROLE_COMPANY", "slack123");
// 		when(userServiceClient.createUser(any(UserCreateRequestDto.class))).thenReturn(dummyUser);
//
// 		// JWT 액세스 토큰 및 리프레시 토큰 생성 모킹
// 		when(jwtTokenProvider.createAccessToken(eq(dummyUser.getUserId()), eq(dummyUser.getRole())))
// 			.thenReturn("accessToken");
// 		when(jwtTokenProvider.createRefreshToken(eq(dummyUser.getUserId())))
// 			.thenReturn("refreshToken");
//
// 		// When - 테스트 실행 (회원가입 서비스 호출)
// 		AuthLoginResponseDto response = authService.register(requestDto);
//
// 		// Then - 검증 (회원가입 결과가 올바르게 반환되었는지 확인)
// 		assertNotNull(response);
// 		assertEquals(1L, response.getUserId());
// 		assertEquals("accessToken", response.getAccessToken());
// 		assertEquals("refreshToken", response.getRefreshToken());
// 	}
//
// 	@Test
// 	public void testLogin_Success() {
// 		// 로그인 성공 테스트
// 		// Given - 로그인 요청 데이터 생성
// 		AuthLoginRequestDto requestDto = new AuthLoginRequestDto("testuser", "password123");
//
// 		// 기존 사용자 정보 설정
// 		UserCreateResponseDto dummyUser = UserCreateResponseDto.builder()
// 			.userId(1L)
// 			.username("testuser")
// 			.password("hashedPassword")
// 			.email("test@example.com")
// 			.role("ROLE_COMPANY")
// 			.slackId("slack123")
// 			.build();
//
// 		// UserServiceClient를 통해 사용자 조회 모킹
// 		when(userServiceClient.getUserByUsername("testuser")).thenReturn(dummyUser);
// 		// 비밀번호 검증 모킹
// 		when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);
// 		// JWT 토큰 생성 모킹
// 		when(jwtTokenProvider.createAccessToken(eq(1L), anyString())).thenReturn("accessToken");
// 		when(jwtTokenProvider.createRefreshToken(1L)).thenReturn("refreshToken");
//
// 		// When - 로그인 서비스 호출
// 		AuthLoginResponseDto response = authService.login(requestDto);
//
// 		// Then - 로그인 결과 검증
// 		assertNotNull(response);
// 		assertEquals(1L, response.getUserId());
// 		assertEquals("accessToken", response.getAccessToken());
// 		assertEquals("refreshToken", response.getRefreshToken());
//
// 		// Mock 객체 호출 검증
// 		verify(userServiceClient).getUserByUsername("testuser");
// 		verify(passwordEncoder).matches("password123", "hashedPassword");
// 		verify(jwtTokenProvider).createAccessToken(1L, "ROLE_COMPANY");
// 		verify(jwtTokenProvider).createRefreshToken(1L);
// 	}
//
// 	@Test
// 	public void testRefreshToken_Success() {
// 		// 리프레시 토큰을 통한 액세스 토큰 갱신 테스트
// 		// Given - 유효한 리프레시 토큰 및 사용자 ID 설정
// 		String refreshToken = "validRefreshToken";
// 		Long userId = 1L;
// 		AuthRefreshRequestDto requestDto = AuthRefreshRequestDto.builder()
// 			.refreshToken(refreshToken)
// 			.build();
//
// 		// 기존 사용자 정보 설정
// 		UserCreateResponseDto dummyUser = UserCreateResponseDto.builder()
// 			.userId(1L)
// 			.username("testuser")
// 			.password("hashedPassword")
// 			.email("test@example.com")
// 			.role("ROLE_COMPANY")
// 			.slackId("slack123")
// 			.build();
//
// 		// 토큰 검증 및 사용자 정보 조회 모킹
// 		when(jwtTokenProvider.validateToken(refreshToken)).thenReturn(true);
// 		when(jwtTokenProvider.getUserIdFromToken(refreshToken)).thenReturn(userId);
// 		when(userServiceClient.getUserById(userId)).thenReturn(dummyUser);
// 		when(jwtTokenProvider.createAccessToken(userId, "ROLE_COMPANY")).thenReturn("newAccessToken");
//
// 		// When - 토큰 갱신 서비스 호출
// 		AuthTokenResponseDto response = authService.refresh(requestDto);
//
// 		// Then - 결과 검증
// 		assertNotNull(response);
// 		assertEquals("newAccessToken", response.getAccessToken());
// 		assertEquals(refreshToken, response.getRefreshToken());
//
// 		// Mock 객체 호출 검증
// 		verify(jwtTokenProvider).validateToken(refreshToken);
// 		verify(jwtTokenProvider).getUserIdFromToken(refreshToken);
// 		verify(userServiceClient).getUserById(userId);
// 		verify(jwtTokenProvider).createAccessToken(userId, "ROLE_COMPANY");
// 	}
// }