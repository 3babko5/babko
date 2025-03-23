package com.business.user.user.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.business.common.application.exception.BusinessLogicException;
import com.business.user.user.application.exception.UserExceptionCode;
import com.business.user.user.domain.entity.User;
import com.business.user.user.domain.repository.UserRepository;
import com.business.user.user.domain.dto.CreateUserRequestDto;
import com.business.user.user.domain.dto.UserResponseDto;
import com.business.user.user.domain.entity.UserType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	@Transactional
	public void createUser(CreateUserRequestDto requestDto) {
		// 중복 체크
		if (userRepository.existsByUsername(requestDto.getUsername())) {
			throw new BusinessLogicException(UserExceptionCode.USERNAME_ALREADY_EXISTS);
		}
		
		if (userRepository.existsByEmail(requestDto.getEmail())) {
			throw new BusinessLogicException(UserExceptionCode.EMAIL_ALREADY_EXISTS);
		}
		
		if (userRepository.existsBySlackId(requestDto.getSlackId())) {
			throw new BusinessLogicException(UserExceptionCode.SLACKID_ALREADY_EXISTS);
		}

		// UserType 변환
		UserType role = UserType.valueOf(requestDto.getRole());

		// 사용자 생성 및 저장
		User user = User.create(
			requestDto.getUsername(),
			requestDto.getPassword(),
			requestDto.getEmail(),
			requestDto.getSlackId(),
			role
		);
		// 시스템 사용자(ID: 0)로 감사 필드 설정
		user.setCreatedBy(0L);
		
		userRepository.save(user);
	}

	@Transactional(readOnly = true)
	public UserResponseDto getUserById(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BusinessLogicException(UserExceptionCode.USER_NOT_FOUND));
		
		return UserResponseDto.fromEntity(user);
	}

	@Transactional(readOnly = true)
	public UserResponseDto getUserByUsername(String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new BusinessLogicException(UserExceptionCode.USER_NOT_FOUND));
		
		return UserResponseDto.fromEntity(user);
	}
}
