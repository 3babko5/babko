package com.business.user.user.domain.repository;

import java.util.Optional;

import jakarta.validation.constraints.NotBlank;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.business.user.user.domain.entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
    boolean existsBySlackId(String slackId);

	// 사용자명 또는 이메일 키워드 검색
	@Query("SELECT u FROM User u " +
		"WHERE (LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
		"OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
		"AND u.deletedAt IS NULL")
	Page<User> searchByKeyword(String keyword, Pageable pageable);
}
