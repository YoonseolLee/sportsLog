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

	public boolean existsByName(String name) {
		String jpql = "SELECT COUNT(b) FROM Board b WHERE b.name = :name";
		Long count = em.createQuery(jpql, Long.class)
			.setParameter("name", name)
			.getSingleResult();
		return count > 0;
	}
}
