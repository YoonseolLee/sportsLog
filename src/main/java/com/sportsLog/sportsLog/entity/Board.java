package com.sportsLog.sportsLog.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Builder;

@Entity
public class Board {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String name;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@Column(nullable = false)
	private LocalDateTime modifiedAt;

	@Column(nullable = true)
	private String description;

	@Column(nullable = false)
	private boolean status = true;

	@Column(nullable = false)
	private int views = 0;

	@Builder
	public Board(String name, String description) {
		this.name= name;
		this.description = description;
	}

	@PrePersist
	public void prePersist() {
		LocalDateTime now = LocalDateTime.now();
		createdAt = now;
		modifiedAt = now;
	}

	@PreUpdate
	public void preUpdate() {
		modifiedAt = LocalDateTime.now();
	}
}

