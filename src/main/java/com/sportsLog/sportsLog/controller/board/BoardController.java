package com.sportsLog.sportsLog.controller.board;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sportsLog.sportsLog.common.SessionConst;
import com.sportsLog.sportsLog.dto.board.AddBoardRequestDto;
import com.sportsLog.sportsLog.entity.Post;
import com.sportsLog.sportsLog.entity.User;
import com.sportsLog.sportsLog.repository.UserRepository;
import com.sportsLog.sportsLog.service.board.BoardService;
import com.sportsLog.sportsLog.service.post.PostService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

	private final PostService postService;
	private final BoardService boardService;
	private final UserRepository userRepository;

	private User addSessionUserToModel(HttpSession session) {
		String loginEmail = (String)session.getAttribute(SessionConst.LOGIN_EMAIL);
		if (loginEmail != null) {
			return userRepository.findByEmail(loginEmail).orElse(null);
		}
		return null;
	}

	@GetMapping("/{board}")
	public String viewBoard(@PathVariable String board, HttpSession session, Model model) {
		User loginUser = addSessionUserToModel(session);
		model.addAttribute("loginUser", loginUser);

		List<Post> posts = postService.findPostsByBoard(board);
		model.addAttribute("posts", posts);

		return "/board/" + board;
	}

	@PostMapping("/create")
	public String createBoard(@ModelAttribute AddBoardRequestDto addBoardRequestDto) {
		boardService.saveBoard(addBoardRequestDto);
		return "redirect:/board/" + addBoardRequestDto.getName();
	}
}
