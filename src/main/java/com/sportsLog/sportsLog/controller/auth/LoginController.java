package com.sportsLog.sportsLog.controller.auth;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.sportsLog.sportsLog.dto.LoginDto;
import com.sportsLog.sportsLog.entity.User;
import com.sportsLog.sportsLog.exception.LoginFailedException;
import com.sportsLog.sportsLog.service.auth.LoginService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class LoginController {

	private final LoginService loginService;

	@GetMapping("/login")
	public String showLoginForm() {
		return "/auth/login";
	}

	@PostMapping("/login")
	public ResponseEntity<Map<String, String>> login(@RequestBody @Validated LoginDto loginDto, BindingResult bindingResult) {
		// Bean Validation
		if (bindingResult.hasErrors()) {
			Map<String, String> errors = new HashMap<>();
			bindingResult.getFieldErrors().forEach(error -> {
				errors.put(error.getField(), error.getDefaultMessage());
			});
			log.info("Errors: {}", errors);
			return ResponseEntity.badRequest().body(errors);
		}

		try {
			// 로그인 로직 수행 및 Custom Validation
			User loginUser = loginService.login(loginDto.getEmail(), loginDto.getPassword());

			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
			loginService.createSession(loginUser, request);

			return ResponseEntity.ok().body(Collections.singletonMap("redirectURL", "/"));
		} catch (LoginFailedException e) {
			return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
		}
	}
}
