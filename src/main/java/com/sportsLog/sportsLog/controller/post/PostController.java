package com.sportsLog.sportsLog.controller.post;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sportsLog.sportsLog.common.SessionConst;
import com.sportsLog.sportsLog.dto.post.AddPostRequestDto;
import com.sportsLog.sportsLog.entity.User;
import com.sportsLog.sportsLog.repository.UserRepository;
import com.sportsLog.sportsLog.service.post.PostService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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

		if (loginUser == null) {
			// 로그인되지 않은 상태라면 로그인 페이지로 리다이렉트
			return "redirect:/auth/login";
		}

		model.addAttribute("loginUser", loginUser);
		model.addAttribute("board", board);
		return "post/createPost";
	}

	@PostMapping("/create")
	public String createPost(@Valid @ModelAttribute AddPostRequestDto addPostRequestDto, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("errorMessage", "게시글 규칙에 알맞게 입력해주세요.");
			return "post/createPost";
		}
		postService.savePost(addPostRequestDto);
		return "redirect:/board/" + addPostRequestDto.getBoard();
	}
}
