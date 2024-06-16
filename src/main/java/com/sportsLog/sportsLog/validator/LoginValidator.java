package com.sportsLog.sportsLog.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.sportsLog.sportsLog.dto.LoginDto;

@Component
public class LoginValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return LoginDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		LoginDto loginDto = (LoginDto) target;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "required", "이메일은 필수 입력 값입니다.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "required", "비밀번호는 필수 입력 값입니다.");

		if (!loginDto.getEmail().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
			errors.rejectValue("email", "invalid", "올바른 이메일 형식을 입력하세요.");
		}

		if (!loginDto.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$")) {
			errors.rejectValue("password", "short", "비밀번호는 영문, 숫자, 특수문자를 포함하여 8~20자리여야 합니다.");
		}
	}
}
