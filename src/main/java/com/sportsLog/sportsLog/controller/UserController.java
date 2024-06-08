package com.sportsLog.sportsLog.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sportsLog.sportsLog.dto.AddUserRequestDto;
import com.sportsLog.sportsLog.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

	// TODO: 회원가입 페이지 - 인증하기 버튼이 가입 가능할때만 활성화 되게함.
	// TODO: try catch와 bean validation 딜레마
	// TODO: user 테이블에 권한 필드 추가

	private final UserService userService;

	@GetMapping("/user/signup")
	public String showSignupForm(Model model) {
		model.addAttribute("user", new AddUserRequestDto());
		return "user/signup";
	}

	@PostMapping("/user/signup")
	public ResponseEntity<String> saveUser(@RequestBody @Validated AddUserRequestDto addUserRequestDto,
		BindingResult bindingResult, Model model) {
		// 실패 로직
		if (bindingResult.hasErrors()) {
			log.info("회원가입 실패");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원가입에 실패하였습니다");
		}

		// 성공 로직
		userService.addUser(addUserRequestDto);
		log.info("회원가입 완료: email = {}", addUserRequestDto.getEmail());
		return ResponseEntity.ok("회원가입에 성공하였습니다");
	}

	@PostMapping("/mailDuplicationValidation")
	public ResponseEntity<Void> validateMailDuplication(@RequestBody Map<String, String> request) {
		String email = request.get("email");
		boolean isMailDuplicated = userService.checkMailDuplication(email);

		if (isMailDuplicated) {
			log.error("메일 중복: {}", email);
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		return ResponseEntity.ok().build();
	}
}
