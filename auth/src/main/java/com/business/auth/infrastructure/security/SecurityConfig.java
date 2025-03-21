package com.business.auth.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
// 주석은 마지막에 다 삭제 하겠습니다!
@Configuration
@EnableWebSecurity //Spring Security를 활성화하는 어노테이션
public class SecurityConfig {

	@Bean
	//@Bean으로 등록되므로 스프링 컨테이너에서 관리
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())  // CSRF 보호 비활성화 (서버가 세션을 저장하지 않으니..필요XX)
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless 설정
			)
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/auth/**").permitAll() // 인증 없이 접근 허용
				.requestMatchers("/user/**").permitAll()
				.anyRequest().authenticated()
			)// JWT를 사용하고 있으므로 stateless한 서버에게는 필요없는 Filter들을 비활성화
			.formLogin(form -> form.disable())  // 폼 로그인 비활성화
			.httpBasic(basic -> basic.disable()) // HTTP Basic 인증 비활성화
			.logout(logout -> logout.disable()); // 로그아웃 기능 비활성화

		return http.build();
	}

	/*
	 * BCryptPasswordEncoder Bean 등록
	 * - 사용자 비밀번호를 안전하게 암호화하기 위해 사용하고
	 * - Spring Security에서 비밀번호 저장 시 BCrypt 암호화를 권장
	 */
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}