package com.sportsLog.sportsLog.controller.auth;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sportsLog.sportsLog.common.SessionConst;
import com.sportsLog.sportsLog.dto.LoginDto;
import com.sportsLog.sportsLog.entity.User;
import com.sportsLog.sportsLog.service.AuthService;
import com.sportsLog.sportsLog.validator.LoginValidator;

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

	// TODO: 자동로그인 기능

	private final AuthService authService;
	private final LoginValidator loginValidator;

	@GetMapping("/login")
	public String showLoginForm() {
		return "/auth/login";
	}

	@PostMapping("/login")
	public ResponseEntity<Map<String, String>> login(@RequestBody @Valid LoginDto loginDto, BindingResult bindingResult, HttpServletRequest request) {
		log.info("검증 시작: {} {}", loginDto.getEmail(), loginDto.getPassword());
		loginValidator.validate(loginDto, bindingResult);
		log.info("검증 완료");

		if (bindingResult.hasErrors()) {
			// 검증 실패 시 에러 메시지를 클라이언트로 전송
			Map<String, String> errors = new HashMap<>();
			bindingResult.getFieldErrors().forEach(error -> {
				errors.put(error.getField(), error.getDefaultMessage());
			});
			bindingResult.getGlobalErrors().forEach(error -> {
				errors.put(error.getObjectName(), error.getDefaultMessage());
			});
			log.info("Errors: {}", errors);
			return ResponseEntity.badRequest().body(errors);
		}

		User loginUser = authService.login(loginDto.getEmail(), loginDto.getPassword());
		if (loginUser == null) {
			Map<String, String> errors = new HashMap<>();
			errors.put("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
			log.info("Global Error - loginFail: 아이디 또는 비밀번호가 맞지 않습니다.");
			return ResponseEntity.badRequest().body(errors);
		}

		HttpSession session = request.getSession(true);
		session.setAttribute(SessionConst.LOGIN_EMAIL, loginUser.getEmail());
		session.setAttribute("nickname", loginUser.getNickname());
		return ResponseEntity.ok().body(Collections.singletonMap("redirectURL", "/"));
	}
}
