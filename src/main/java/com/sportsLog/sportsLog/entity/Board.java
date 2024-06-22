package com.sportsLog.sportsLog.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.sportsLog.sportsLog.common.BoardStatus;
import com.sportsLog.sportsLog.entity.listener.BoardEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EntityListeners(BoardEntityListener.class)
public class Board {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Post> posts = new ArrayList<>();

	@Column(nullable = false, unique = true)
	private String name;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@Column(nullable = false)
	private LocalDateTime modifiedAt;

	@Column(nullable = true)
	private String description;

	@Column(nullable = false)
	private int postCount = 0;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private BoardStatus status;

	@Builder
	public Board(String name, String description, BoardStatus status) {
		this.name = name;
		this.description = description;
		this.status = status;
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

	public void incrementPostCount() {
		this.postCount++;
	}

	public void decrementPostCount() {
		if (this.postCount > 0) {
			this.postCount--;
		}
	}
}

