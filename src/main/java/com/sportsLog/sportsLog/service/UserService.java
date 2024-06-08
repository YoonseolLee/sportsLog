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
import com.sportsLog.sportsLog.service.mail.MailSendService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

	private final MailSendService mailSendService;

	@PersistenceContext
	private EntityManager em;

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

			em.persist(user);
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

	/**
	 * DB에 동일한 email에 저장되어 있으면 true를 반환한다.
	 * cf. 특정 조건을 만족하는 엔티티의 개수를 반환받아야 하므로 Long 타입을 사용한다.
	 * @param email
	 * @return
	 */
	public boolean checkMailDuplication(String email) {
		String jpql = "SELECT COUNT(u) FROM User u WHERE u.email = :email";
		TypedQuery<Long> query = em.createQuery(jpql, Long.class);
		query.setParameter("email", email);

		try {
			Long count = query.getSingleResult();
			return count > 0;
		} catch (NoResultException e) {
			return false;
		}
	}
}
