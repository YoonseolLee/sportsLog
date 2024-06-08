package com.sportsLog.sportsLog.service;

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
public class UserService {

	private final MailSendService mailSendService;
	private final UserRepository userRepository;

	@Transactional
	public Long addUser(AddUserRequestDto addUserRequestDto) {
		boolean isValid = isSignupFormInputValid(addUserRequestDto);

		// TODO: 서버 검증 실패 시 alert
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
		// TODO: 이메일 중복 검증 추가
		boolean isEmailVerified = mailSendService.checkAuthNum(addUserRequestDto.getEmail(),
			addUserRequestDto.getAuthNumber());
		if (!isEmailVerified) {
			return false;
		}

		if (!addUserRequestDto.getPassword().equals(addUserRequestDto.getConfirmPassword())) {
			return false;
		}
		log.info("isSignupFormInputValid 통과");
		return true;
	}

	public boolean checkMailDuplication(String email) {
		Long count = userRepository.countByEmail(email);
		return count > 1;
	}
}
