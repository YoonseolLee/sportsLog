package com.sportsLog.sportsLog.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sportsLog.sportsLog.common.SessionConst;
import com.sportsLog.sportsLog.dto.LoginDto;
import com.sportsLog.sportsLog.entity.User;
import com.sportsLog.sportsLog.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class LoginController {

	// TODO: 회원가입 시 닉네임 + 검증
	// TODO: favicon 넣기

	private final AuthService authService;

	@GetMapping("/login")
	public String showLoginForm() {
		return "/auth/login";
	}

	// TODO: 인터셉터 추가
	// TODO: 로그인 시 검증 로직
	@PostMapping("/login")
	public String login(@Valid @ModelAttribute LoginDto loginDto, BindingResult bindingResult,
		@RequestParam(name = "redirectURL", defaultValue = "/") String redirectURL, HttpServletRequest request) {
		if (bindingResult.hasErrors()) {
			return "auth/login";
		}
		User loginUser = authService.login(loginDto.getEmail(), loginDto.getPassword());
		log.info("login: {} ", loginUser);

		if (loginUser == null) {
			bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
			return "auth/login";
		}

		// 로그인 성공 처리
		// 세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
		HttpSession session = request.getSession(true);

		// 세션에 로그인 회원 정보 보관
		session.setAttribute(SessionConst.LOGIN_EMAIL, loginUser.getEmail());
		return "redirect:" + redirectURL;
	}
}
