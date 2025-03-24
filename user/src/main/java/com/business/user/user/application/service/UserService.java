package com.business.user.user.application.service;

import static com.business.user.user.application.mapper.UserMapper.*;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.business.common.application.exception.BusinessLogicException;
import com.business.user.user.application.dto.request.CreateUserRequestDto;
import com.business.user.user.application.dto.request.UserSearchRequestDto;
import com.business.user.user.application.dto.request.UserUpdateRequestDto;
import com.business.user.user.application.dto.response.UserDetailResponseDto;
import com.business.user.user.application.dto.response.UserPageResponseDto;
import com.business.user.user.application.dto.response.UserResponseDto;
import com.business.user.user.application.exception.UserExceptionCode;
import com.business.user.user.domain.entity.User;
import com.business.user.user.domain.entity.UserType;
import com.business.user.user.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	/**
	 * 회원 생성 (auth-service에서 사용)
	 */
	@Transactional
	public void createUser(CreateUserRequestDto requestDto) {
		if (userRepository.existsByUsername(requestDto.getUsername())) {
			throw new BusinessLogicException(UserExceptionCode.USERNAME_ALREADY_EXISTS);
		}
		if (userRepository.existsByEmail(requestDto.getEmail())) {
			throw new BusinessLogicException(UserExceptionCode.EMAIL_ALREADY_EXISTS);
		}
		if (userRepository.existsBySlackId(requestDto.getSlackId())) {
			throw new BusinessLogicException(UserExceptionCode.SLACKID_ALREADY_EXISTS);
		}

		User user = User.create(
			requestDto.getUsername(),
			requestDto.getPassword(),
			requestDto.getEmail(),
			requestDto.getSlackId(),
			UserType.valueOf(requestDto.getRole())
		);
		user.createdBy(0L); // 시스템 계정

		userRepository.save(user);
	}
	/**
	 * 사용자명(username)으로 조회 (auth-service에서 사용)
	 */
	@Transactional(readOnly = true)
	public UserResponseDto getUserByUsername(String username) {
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> new BusinessLogicException(UserExceptionCode.USER_NOT_FOUND));

		return toResponseDto(user);
	}
	/**
	 * 사용자 ID로 조회 + 권한 검사
	 * - 마스터: 모두 조회 가능
	 * - 그 외: 본인만 가능
	 *
	 */
	@Transactional(readOnly = true)
	public UserDetailResponseDto getUserByIdWithAccessCheck(Long targetUserId, Long requesterId, String requesterRole) {
		User user = userRepository.findById(targetUserId)
			.orElseThrow(() -> new BusinessLogicException(UserExceptionCode.USER_NOT_FOUND));

		if (!requesterId.equals(targetUserId) && !requesterRole.equals("ROLE_MASTER")) {
			throw new BusinessLogicException(UserExceptionCode.USER_ACCESS_DENIED);
		}

		return UserDetailResponseDto.from(user);
	}
	/**
	 * 사용자 목록 조회 (검색 + 정렬 + 페이징)
	 * - 마스터 전용
	 */
	@Transactional(readOnly = true)
	public Page<UserPageResponseDto> searchUsers(UserSearchRequestDto requestDto) {
		int size = switch (requestDto.getSize()) {
			case 10, 30, 50 -> requestDto.getSize();
			default -> 10;
		};

		String sortBy = switch (requestDto.getSortBy()) {
			case "updatedAt" -> "updatedAt";
			default -> "createdAt";
		};

		Pageable pageable = PageRequest.of(requestDto.getPage(), size, Sort.by(Sort.Direction.DESC, sortBy));

		Page<User> users = userRepository.searchByKeyword(requestDto.getKeyword(), pageable);

		return users.map(UserPageResponseDto::from);
	}
	/**
	 * 사용자 수정
	 * - 마스터만 가능
	 */
	@Transactional
	public void updateUser(Long userId, UserUpdateRequestDto requestDto, Long updatedBy) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new BusinessLogicException(UserExceptionCode.USER_NOT_FOUND));

		// 사용자 정보 업데이트
		user.updateInfo(
			requestDto.getEmail(),
			requestDto.getSlackId(),
			UserType.valueOf(requestDto.getRole())
		);
		user.updatedBy(updatedBy);
	}
	/**
	 * 사용자 삭제 (Soft Delete)
	 * - 마스터만 가능
	 */
	@Transactional
	public void deleteUser(Long userId, Long deletedBy) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new BusinessLogicException(UserExceptionCode.USER_NOT_FOUND));

		user.deletedBy(deletedBy); // 삭제자 정보와 삭제 시간 저장
	}
}