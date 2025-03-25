package com.business.user.user.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.business.user.user.domain.entity.User;
import com.business.user.user.domain.repository.UserRepository;


@Repository
public class UserRepositoryImpl implements UserRepository {

	private final UserJpaRepository userJpaRepository;

	public UserRepositoryImpl(UserJpaRepository userJpaRepository) {
		this.userJpaRepository = userJpaRepository;
	}

	@Override
	public Optional<User> findById(Long userId) {
		return userJpaRepository.findById(userId);
	}

	@Override
	public Optional<User> findByUsername(String username) {
		return userJpaRepository.findByUsername(username);
	}

	@Override
	public boolean existsByUsername(String username) {
		return userJpaRepository.existsByUsername(username);
	}

	@Override
	public boolean existsByEmail(String email) {
		return userJpaRepository.existsByEmail(email);
	}

	@Override
	public boolean existsBySlackId(String slackId) {
		return userJpaRepository.existsBySlackId(slackId);
	}

	@Override
	public Page<User> searchByKeyword(String keyword, Pageable pageable) {
		return userJpaRepository.searchByKeyword(keyword, pageable);
	}

	@Override
	public User save(User user) {
		return userJpaRepository.save(user);
	}
}