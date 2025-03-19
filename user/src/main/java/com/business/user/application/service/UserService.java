package com.business.user.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.business.common.application.exception.BusinessLogicException;
import com.business.user.application.dto.mapper.UserMapper;
import com.business.user.application.dto.request.UserSignupRequestDto;
import com.business.user.application.dto.response.UserSigninResponseDto;
import com.business.user.application.dto.response.UserSignupResponseDto;
import com.business.user.application.exception.UserExceptionCode;
import com.business.user.domain.entity.User;
import com.business.user.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

	private final UserRepository userRepository;

	/**
	 *  회원가입 처리
	 */
	public UserSignupResponseDto registerUser(UserSignupRequestDto requestDto) {
		validateDuplicateUser(requestDto); // 중복 체크

		// Auth 서비스에서 해싱된 비밀번호를 받아옴
		String encryptedPassword = requestDto.getPassword(); // 암호화된 비밀번호를 받아옴

		// User 엔티티를 builder 패턴을 사용하여 생성
		User user = User.builder()
			.username(requestDto.getUsername())
			.password(encryptedPassword)  // 암호화된 비밀번호
			.email(requestDto.getEmail())
			.slackId(requestDto.getSlackId())
			.role(requestDto.getRole())
			.build();

		// DB에 저장
		userRepository.save(user);

		// DTO로 변환해서 반환
		return UserMapper.toSignupResponseDto(user);
	}


	/**
	 *  유저 정보 조회
	 */
	public UserSigninResponseDto getUserByUsername(String username) {
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> new BusinessLogicException(UserExceptionCode.USER_NOT_FOUND));

		return UserMapper.toSigninResponseDto(user);
	}

	/**
	 *  중복 유저 체크
	 */
	private void validateDuplicateUser(UserSignupRequestDto requestDto) {
		if (userRepository.existsByUsername(requestDto.getUsername())) {
			throw new BusinessLogicException(UserExceptionCode.USERNAME_ALREADY_EXISTS);
		}
		if (userRepository.existsByEmail(requestDto.getEmail())) {
			throw new BusinessLogicException(UserExceptionCode.EMAIL_ALREADY_EXISTS);
		}
	}
}