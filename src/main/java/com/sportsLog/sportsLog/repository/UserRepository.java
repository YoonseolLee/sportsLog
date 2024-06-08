package com.sportsLog.sportsLog.repository;

import org.springframework.stereotype.Repository;

import com.sportsLog.sportsLog.entity.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class UserRepository {

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void save(User user) {
		em.persist(user);
	}

	public Long countByEmail(String email) {
		String jpql = "SELECT COUNT(u) FROM User u WHERE u.email = :email";
		TypedQuery<Long> query = em.createQuery(jpql, Long.class);
		query.setParameter("email", email);

		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return 0L;
		}
	}

	public User findById(Long id) {
		try {
			return em.find(User.class, id);
		} catch(NoResultException e) {
			return null;
		}
	}
}
