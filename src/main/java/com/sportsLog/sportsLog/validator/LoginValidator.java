package com.sportsLog.sportsLog.validator;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.sportsLog.sportsLog.dto.LoginDto;
import com.sportsLog.sportsLog.entity.User;
import com.sportsLog.sportsLog.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoginValidator implements Validator {

	private final UserRepository userRepository;

	@Override
	public boolean supports(Class<?> clazz) {
		return LoginDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		LoginDto loginDto = (LoginDto)target;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "required", "이메일은 필수 입력 값입니다.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "required", "비밀번호는 필수 입력 값입니다.");

		if (!loginDto.getEmail().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
			errors.rejectValue("email", "invalid", "올바른 이메일 형식을 입력하세요.");
		}

		if (!loginDto.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$")) {
			errors.rejectValue("password", "short", "비밀번호는 영문, 숫자, 특수문자를 포함하여 8~20자리여야 합니다.");
		}

		// 이메일과 비밀번호 일치 여부 검증
		Optional<User> userOpt = userRepository.findByEmail(loginDto.getEmail());
		if (userOpt.isPresent()) {
			User user = userOpt.get();
			if (!BCrypt.checkpw(loginDto.getPassword(), user.getPassword())) {
				errors.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
			}
		} else {
			errors.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
		}
	}
}
