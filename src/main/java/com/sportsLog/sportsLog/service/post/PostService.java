package com.sportsLog.sportsLog.service.post;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sportsLog.sportsLog.dto.post.AddPostRequestDto;
import com.sportsLog.sportsLog.entity.Board;
import com.sportsLog.sportsLog.entity.Post;
import com.sportsLog.sportsLog.entity.User;
import com.sportsLog.sportsLog.exception.PostException;
import com.sportsLog.sportsLog.repository.BoardRepository;
import com.sportsLog.sportsLog.repository.PostRepository;
import com.sportsLog.sportsLog.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

	private static final String INVALID_USER_EMAIL = "저장되어 있지 않은 이메일입니다.";
	private static final String INVALID_BOARD_NAME = "저장되어 있지 않은 게시판입니다.";

	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final BoardRepository boardRepository;

	@Transactional
	public void savePost(AddPostRequestDto addPostRequestDto) {
		User user = findUserByEmail(addPostRequestDto.getEmail());
		Board board = findBoardByName(addPostRequestDto.getBoard());

		Post post = Post.builder()
			.title(addPostRequestDto.getTitle())
			.content(addPostRequestDto.getContent())
			.user(user)
			.views(0)
			.likes(0)
			.board(board)
			.build();

		user.addPost(post, board);
		postRepository.save(post);
	}

	private User findUserByEmail(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new PostException(INVALID_USER_EMAIL));
	}

	private Board findBoardByName(String boardName) {
		return boardRepository.findByName(boardName)
			.orElseThrow(() -> new PostException(INVALID_BOARD_NAME));
	}

	public List<Post> findPostsByBoard(Board board) {
		return postRepository.findByBoard(board);
	}
}
