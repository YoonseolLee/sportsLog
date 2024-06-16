package com.sportsLog.sportsLog.controller.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
	public ResponseEntity<Void> logout(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		logoutService.logout(session);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
