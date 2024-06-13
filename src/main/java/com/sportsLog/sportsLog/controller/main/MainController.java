package com.sportsLog.sportsLog.controller.main;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sportsLog.sportsLog.common.SessionConst;
import com.sportsLog.sportsLog.entity.User;
import com.sportsLog.sportsLog.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class MainController {

	private final UserRepository userRepository;

	@GetMapping
	public String showMainPage(HttpSession session, Model model) {
		String loginEmail = (String)session.getAttribute(SessionConst.LOGIN_EMAIL);
		if (loginEmail != null) {
			User loginUser = userRepository.findByEmail(loginEmail).orElse(null);
			model.addAttribute("loginUser", loginUser);
		}
		return "main/main";
	}
}
