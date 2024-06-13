package com.sportsLog.sportsLog.service.auth;

import java.time.LocalDateTime;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import com.sportsLog.sportsLog.common.Role;
import com.sportsLog.sportsLog.dto.AddUserRequestDto;
import com.sportsLog.sportsLog.entity.User;
import com.sportsLog.sportsLog.repository.UserRepository;
import com.sportsLog.sportsLog.service.mail.MailSendService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class SignupService {

	private final MailSendService mailSendService;
	private final UserRepository userRepository;

	@Transactional
	public Long addUser(AddUserRequestDto addUserRequestDto) {
		boolean isValid = isSignupFormInputValid(addUserRequestDto);

		if (isValid) {
			String encodedPassword = BCrypt.hashpw(addUserRequestDto.getPassword(), BCrypt.gensalt());

			User user = User.builder()
				.email(addUserRequestDto.getEmail())
				.password(encodedPassword)
				.birthdate(addUserRequestDto.getBirthdate())
				.role(Role.USER.name())
				.emailVerified(true)
				.passwordChangeDatetime(LocalDateTime.now())
				.lastLoginDatetime(LocalDateTime.now())
				.accountCreatedDateTime(LocalDateTime.now())
				.accountDeletedDateTime(null)
				.isAccountDeleted(false)
				.build();

			userRepository.save(user);
			return user.getId();
		} else {
			throw new IllegalArgumentException("입력된 값이 유효하지 않습니다.");
		}
	}

	@Transactional
	public boolean isSignupFormInputValid(@RequestBody @Validated AddUserRequestDto addUserRequestDto) {
		log.info("Starting validation for email: {}", addUserRequestDto.getEmail());

		boolean isEmailVerified = mailSendService.checkAuthNum(addUserRequestDto.getEmail(), addUserRequestDto.getAuthNumber());
		if (!isEmailVerified) {
			log.error("Email verification failed for email: {}", addUserRequestDto.getEmail());
			return false;
		}

		if (!addUserRequestDto.getPassword().equals(addUserRequestDto.getConfirmPassword())) {
			log.error("Password and confirm password do not match for email: {}", addUserRequestDto.getEmail());
			return false;
		}

		boolean isDuplicated = checkMailDuplication(addUserRequestDto.getEmail());
		if (isDuplicated) {
			log.error("Email is already in use: {}", addUserRequestDto.getEmail());
			return false;
		}

		log.info("Validation passed for email: {}", addUserRequestDto.getEmail());
		return true;
	}

	public boolean checkMailDuplication(String email) {
		Long count = userRepository.countByEmail(email);
		return count > 0;
	}
}
