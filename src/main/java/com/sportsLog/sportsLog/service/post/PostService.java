package com.sportsLog.sportsLog.service.post;

import org.springframework.stereotype.Service;

import com.sportsLog.sportsLog.dto.post.AddPostRequestDto;
import com.sportsLog.sportsLog.entity.Post;
import com.sportsLog.sportsLog.entity.User;
import com.sportsLog.sportsLog.repository.PostRepository;
import com.sportsLog.sportsLog.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;

	@Transactional
	public void savePost(AddPostRequestDto addPostRequestDto) {
		User user = userRepository.findByEmail(addPostRequestDto.getEmail())
			.orElseThrow(() -> new IllegalArgumentException("Invalid user email"));

		Post post = Post.builder()
			.title(addPostRequestDto.getTitle())
			.content(addPostRequestDto.getContent())
			.board(addPostRequestDto.getBoard())
			.user(user)
			.build();
		user.addPost(post);
		postRepository.save(post);
	}
}
