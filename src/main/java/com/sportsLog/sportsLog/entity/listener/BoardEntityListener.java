package com.sportsLog.sportsLog.entity.listener;

import java.time.LocalDateTime;

import com.sportsLog.sportsLog.entity.Board;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class BoardEntityListener {

	@PrePersist
	public void prePersist(Board board) {
		LocalDateTime now = LocalDateTime.now();
		board.setCreatedAt(now);
		board.setModifiedAt(now);
	}

	@PreUpdate
	public void preUpdate(Board board) {
		board.setModifiedAt(LocalDateTime.now());
	}
}
