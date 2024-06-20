package com.sportsLog.sportsLog.entity;

import java.time.LocalDateTime;

import com.sportsLog.sportsLog.entity.listener.PostEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EntityListeners(PostEntityListener.class)
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	@Lob
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;

	private int views;
	private int likes;
	private String board;
	private boolean deleted = false;

	@Builder
	public Post(String title, String content, User user, int views, int likes, String board)  {
		this.title = title;
		this.content = content;
		this.user = user;
		this.views = views;
		this.likes = likes;
		this.board = board;
	}

	public void deletePost() {
		this.deleted = true;
	}
}