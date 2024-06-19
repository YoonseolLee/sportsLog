package com.sportsLog.sportsLog.controller.board;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
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
@RequestMapping("/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

	// TODO: 로그인 안했을 때, 게시글 쓰기 불허
	// TODO: PostController 분리

	private final PostService postService;
	private final UserRepository userRepository;

	private ModelAndView addSessionUserToModel(HttpSession session, String viewName) {
		ModelAndView modelAndView = new ModelAndView(viewName);
		String loginEmail = (String) session.getAttribute(SessionConst.LOGIN_EMAIL);
		if (loginEmail != null) {
			User loginUser = userRepository.findByEmail(loginEmail).orElse(null);
			modelAndView.addObject("loginUser", loginUser);
		}
		return modelAndView;
	}

	@GetMapping("/baseball")
	public ModelAndView baseballBoard(HttpSession session) {
		return addSessionUserToModel(session, "/board/baseball");
	}

	@GetMapping("/basketball")
	public ModelAndView basketballBoard(HttpSession session) {
		return addSessionUserToModel(session, "/board/basketball");
	}

	@GetMapping("/soccer")
	public ModelAndView soccerBoard(HttpSession session) {
		return addSessionUserToModel(session, "/board/soccer");
	}

	@GetMapping("/americanFootball")
	public ModelAndView americanFootballBoard(HttpSession session) {
		return addSessionUserToModel(session, "/board/americanFootball");
	}

	@GetMapping("/mma")
	public ModelAndView mmaBoard(HttpSession session) {
		return addSessionUserToModel(session, "/board/mma");
	}

	@GetMapping("/boxing")
	public ModelAndView boxingBoard(HttpSession session) {
		return addSessionUserToModel(session, "/board/boxing");
	}

	@GetMapping("/createPost")
	public ModelAndView createPost(@RequestParam("board") String board, HttpSession session) {
		ModelAndView mav = addSessionUserToModel(session, "/board/createPost");
		mav.addObject("board", board);
		return mav;
	}

	@PostMapping("/createPost")
	public RedirectView createPost(@RequestParam("board") String board,
		@RequestParam("title") String title,
		@RequestParam("content") String content,
		@RequestParam("email") String email) {
		log.info("createPost 호출완료");

		AddPostRequestDto addPostRequestDto = new AddPostRequestDto();
		addPostRequestDto.setBoard(board);
		addPostRequestDto.setTitle(title);
		addPostRequestDto.setContent(content);
		addPostRequestDto.setEmail(email);
		log.info("email: {} ", addPostRequestDto.getEmail());

		postService.savePost(addPostRequestDto);

		return new RedirectView("/board/" + board);
	}
}
