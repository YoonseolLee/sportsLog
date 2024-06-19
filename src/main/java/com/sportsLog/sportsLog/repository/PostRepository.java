package com.sportsLog.sportsLog.repository;

import org.springframework.stereotype.Repository;

import com.sportsLog.sportsLog.entity.Post;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
public class PostRepository {

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void save(Post post) {
		em.persist(post);
	}
}
