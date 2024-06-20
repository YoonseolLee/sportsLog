package com.sportsLog.sportsLog.entity.listener;

import java.time.LocalDateTime;

import com.sportsLog.sportsLog.entity.Post;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class PostEntityListener {

	@PrePersist
	public void prePersist(Post post) {
		LocalDateTime now = LocalDateTime.now();
		post.setCreatedDate(now);
		post.setModifiedDate(now);
	}

	@PreUpdate
	public void preUpdate(Post post) {
		post.setModifiedDate(LocalDateTime.now());
	}
}
