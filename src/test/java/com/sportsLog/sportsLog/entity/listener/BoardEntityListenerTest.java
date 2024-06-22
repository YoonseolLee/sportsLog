package com.sportsLog.sportsLog.entity.listener;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.sportsLog.sportsLog.common.BoardStatus;
import com.sportsLog.sportsLog.entity.Board;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@SpringBootTest
public class BoardEntityListenerTest {

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	@Transactional
	public void testPrePersist() {
		// Given
		Board board = Board.builder()
			.name("General")
			.description("General Discussion")
			.status(BoardStatus.ACTIVE)
			.build();

		LocalDateTime beforePersist = LocalDateTime.now();

		// When
		entityManager.persist(board);
		entityManager.flush();

		// Then
		assertNotNull(board.getCreatedAt());
		assertNotNull(board.getModifiedAt());
		assertTrue(board.getCreatedAt().isAfter(beforePersist) || board.getCreatedAt().isEqual(beforePersist));
		assertTrue(board.getModifiedAt().isAfter(beforePersist) || board.getModifiedAt().isEqual(beforePersist));
	}

	@Test
	@Transactional
	public void testPreUpdate() {
		// Given
		Board board = Board.builder()
			.name("General")
			.description("General Discussion")
			.status(BoardStatus.ACTIVE)
			.build();

		entityManager.persist(board);
		entityManager.flush();

		LocalDateTime createdDate = board.getCreatedAt();

		// When
		board.setDescription("Updated Discussion");
		LocalDateTime beforeUpdate = LocalDateTime.now();

		entityManager.merge(board);
		entityManager.flush();

		// Then
		assertNotNull(board.getModifiedAt());
		assertTrue(board.getModifiedAt().isAfter(createdDate));
		assertTrue(board.getModifiedAt().isAfter(beforeUpdate) || board.getModifiedAt().isEqual(beforeUpdate));
	}
}
