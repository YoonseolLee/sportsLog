package com.sportsLog.sportsLog.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sportsLog.sportsLog.entity.Board;
import com.sportsLog.sportsLog.entity.Post;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Repository
public class PostRepository {

	@PersistenceContext
	private EntityManager em;

	public void save(Post post) {
		em.persist(post);
	}

	public List<Post> findByBoard(Board board) {
		TypedQuery<Post> query = em.createQuery("SELECT p FROM Post p WHERE p.board = :board ORDER BY p.createdDate DESC", Post.class);
		query.setParameter("board", board);
		return query.getResultList();
	}
}
