package com.sportsLog.sportsLog.controller.post;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.sportsLog.sportsLog.common.SessionConst;
import com.sportsLog.sportsLog.dto.post.AddPostRequestDto;
import com.sportsLog.sportsLog.entity.User;
import com.sportsLog.sportsLog.repository.UserRepository;
import com.sportsLog.sportsLog.service.post.PostService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
@Slf4j
public class PostController {

	private final PostService postService;
	private final UserRepository userRepository;

	private User addSessionUserToModel(HttpSession session) {
		String loginEmail = (String)session.getAttribute(SessionConst.LOGIN_EMAIL);
		if (loginEmail != null) {
			return userRepository.findByEmail(loginEmail).orElse(null);
		}
		return null;
	}

	@GetMapping("/create")
	public String createPostForm(@RequestParam("board") String board, HttpSession session, Model model) {
		User loginUser = addSessionUserToModel(session);
		model.addAttribute("loginUser", loginUser);
		model.addAttribute("board", board);
		return "post/createPost";
	}

	@PostMapping("/create")
	public RedirectView createPost(AddPostRequestDto addPostRequestDto) {
		postService.savePost(addPostRequestDto);
		return new RedirectView("/board/" + addPostRequestDto.getBoard());
	}
}
