package com.sportsLog.sportsLog.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sportsLog.sportsLog.service.auth.LogoutService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class LogoutController {

	private final LogoutService logoutService;

	@PostMapping("/logout")
	public String logout(HttpServletRequest request, @RequestParam String redirectUrl) {
		HttpSession session = request.getSession(false);
		logoutService.logout(session);
		return "redirect:" + redirectUrl;
	}
}
