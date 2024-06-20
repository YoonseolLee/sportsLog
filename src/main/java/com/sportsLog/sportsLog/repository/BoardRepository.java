package com.sportsLog.sportsLog.repository;

import org.springframework.stereotype.Repository;

import com.sportsLog.sportsLog.entity.Board;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
public class BoardRepository {

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void save(Board board) {
		em.persist(board);
	}
}
