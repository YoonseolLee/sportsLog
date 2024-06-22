package com.sportsLog.sportsLog.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.sportsLog.sportsLog.entity.Board;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
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

	public Optional<Board> findByName(String name) {
		try {
			return Optional.of(em.createQuery("SELECT b FROM Board b WHERE b.name = :name", Board.class)
				.setParameter("name", name)
				.getSingleResult());
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}
}
