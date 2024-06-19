package com.sportsLog.sportsLog.service.auth;

import java.time.LocalDateTime;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sportsLog.sportsLog.common.Role;
import com.sportsLog.sportsLog.dto.AddUserRequestDto;
import com.sportsLog.sportsLog.entity.User;
import com.sportsLog.sportsLog.entity.UserStatus;
import com.sportsLog.sportsLog.exception.SignupFailedException;
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
		validateSignupForm(addUserRequestDto);

		String encodedPassword = BCrypt.hashpw(addUserRequestDto.getPassword(), BCrypt.gensalt());

		UserStatus userStatus = UserStatus.builder()
			.emailVerified(addUserRequestDto.isEmailVerified())
			.passwordChangeDatetime(LocalDateTime.now())
			.lastLoginDatetime(LocalDateTime.now())
			.accountCreatedDateTime(LocalDateTime.now())
			.isAccountDeleted(false)
			.build();

		User user = User.builder()
			.email(addUserRequestDto.getEmail())
			.password(encodedPassword)
			.birthdate(addUserRequestDto.getBirthdate())
			.nickname(addUserRequestDto.getNickname())
			.role(Role.USER.name())
			.userStatus(userStatus)
			.build();

		userStatus.setUser(user);

		userRepository.save(user);
		return user.getId();
	}

	public void validateSignupForm(AddUserRequestDto addUserRequestDto) {
		log.info("회원가입 검증을 시작합니다.: {}", addUserRequestDto.getEmail());

		if (!mailSendService.checkAuthNum(addUserRequestDto.getEmail(), addUserRequestDto.getAuthNumber())) {
			log.error("이메일 검증 실패: {}", addUserRequestDto.getEmail());
			throw new SignupFailedException("이메일 인증에 실패하였습니다.");
		}

		if (!addUserRequestDto.getPassword().equals(addUserRequestDto.getConfirmPassword())) {
			log.error("비밀번호와 비밀번호 확인 검증 실패: {}", addUserRequestDto.getEmail());
			throw new SignupFailedException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
		}

		if (checkMailDuplication(addUserRequestDto.getEmail())) {
			log.error("이메일 중복 여부 검증 실패: {}", addUserRequestDto.getEmail());
			throw new SignupFailedException("이미 사용 중인 이메일입니다.");
		}

		if (checkNicknameDuplication(addUserRequestDto.getNickname())) {
			log.error("닉네임 중복 여부 검증 실패: {}", addUserRequestDto.getNickname());
			throw new SignupFailedException("이미 사용 중인 닉네임입니다.");
		}
		log.info("회원가입 검증을 통과하였습니다.: {}", addUserRequestDto.getEmail());
	}

	public boolean checkMailDuplication(String email) {
		return userRepository.countByEmail(email) > 0;
	}

	public boolean checkNicknameDuplication(String nickname) {
		return userRepository.existsByNickName(nickname);
	}
}
