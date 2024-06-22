package com.sportsLog.sportsLog.entity.listener;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.sportsLog.sportsLog.common.BoardStatus;
import com.sportsLog.sportsLog.entity.Board;
import com.sportsLog.sportsLog.entity.Post;
import com.sportsLog.sportsLog.entity.User;
import com.sportsLog.sportsLog.entity.UserStatus;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@SpringBootTest
public class PostEntityListenerTest {

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	@Transactional
	public void testPrePersist() {
		// Given
		UserStatus userStatus = UserStatus.createUserStatus(
			true,
			0,
			LocalDateTime.now().minusDays(1),
			LocalDateTime.now().minusHours(1),
			LocalDateTime.now().minusMonths(1),
			null,
			false
		);

		User user = User.createUser(
			"testuser@example.com",
			"password",
			LocalDate.of(1990, 1, 1),
			"USER",
			"testuser",
			userStatus
		);

		userStatus.setUser(user);

		Board board = Board.builder()
			.name("General")
			.description("General Discussion")
			.status(BoardStatus.ACTIVE)
			.build();

		entityManager.persist(user);
		entityManager.persist(board);

		Post post = Post.builder()
			.title("Test Title")
			.content("Test Content")
			.user(user)
			.views(0)
			.likes(0)
			.board(board)
			.build();

		LocalDateTime beforePersist = LocalDateTime.now();

		// When
		entityManager.persist(post);
		entityManager.flush();

		// Then
		assertNotNull(post.getCreatedDate());
		assertNotNull(post.getModifiedDate());
		assertTrue(post.getCreatedDate().isAfter(beforePersist) || post.getCreatedDate().isEqual(beforePersist));
		assertTrue(post.getModifiedDate().isAfter(beforePersist) || post.getModifiedDate().isEqual(beforePersist));
	}

	@Test
	@Transactional
	public void testPreUpdate() {
		// Given
		UserStatus userStatus = UserStatus.createUserStatus(
			true,
			0,
			LocalDateTime.now().minusDays(1),
			LocalDateTime.now().minusHours(1),
			LocalDateTime.now().minusMonths(1),
			null,
			false
		);

		User user = User.createUser(
			"testuser@example.com",
			"password",
			LocalDate.of(1990, 1, 1),
			"USER",
			"testuser",
			userStatus
		);

		userStatus.setUser(user);

		Board board = Board.builder()
			.name("General")
			.description("General Discussion")
			.status(BoardStatus.ACTIVE)
			.build();

		entityManager.persist(user);
		entityManager.persist(board);

		Post post = Post.builder()
			.title("Original Title")
			.content("Original Content")
			.user(user)
			.views(0)
			.likes(0)
			.board(board)
			.build();

		entityManager.persist(post);
		entityManager.flush();

		LocalDateTime createdDate = post.getCreatedDate();

		// When
		post.setTitle("Updated Title");
		post.setContent("Updated Content");

		LocalDateTime beforeUpdate = LocalDateTime.now();

		entityManager.merge(post);
		entityManager.flush();

		// Then
		assertNotNull(post.getModifiedDate());
		assertTrue(post.getModifiedDate().isAfter(createdDate));
		assertTrue(post.getModifiedDate().isAfter(beforeUpdate) || post.getModifiedDate().isEqual(beforeUpdate));
	}
}