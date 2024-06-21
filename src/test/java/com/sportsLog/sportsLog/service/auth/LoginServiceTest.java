package com.sportsLog.sportsLog.service.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.sportsLog.sportsLog.common.Role;
import com.sportsLog.sportsLog.common.SessionConst;
import com.sportsLog.sportsLog.entity.User;
import com.sportsLog.sportsLog.exception.LoginFailedException;
import com.sportsLog.sportsLog.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@SpringBootTest
class LoginServiceTest {

	@Autowired
	private LoginService loginService;

	@MockBean
	private UserRepository userRepository;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpSession session;

	private User user;

	@BeforeEach
	void setUp() {
		user = User.builder()
			.email("test@example.com")
			.nickname("testnickname")
			.role(Role.USER.name())
			.password(BCrypt.hashpw("password", BCrypt.gensalt()))
			.build();
	}

	@Test
	void testLoginSuccessful() {
		// given
		when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

		// when
		User result = loginService.login("test@example.com", "password");

		// then
		assertNotNull(result);
		assertEquals("test@example.com", result.getEmail());
	}

	@Test
	void testLoginFailedWithInvalidPassword() {
		// given
		when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

		// when / then
		LoginFailedException exception = assertThrows(LoginFailedException.class, () -> {
			loginService.login("test@example.com", "wrongpassword");
		});

		assertEquals("비밀번호가 일치하지 않습니다.", exception.getMessage());
	}

	@Test
	void testLoginFailedWithNonExistentUser() {
		// given
		when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

		// when / then
		LoginFailedException exception = assertThrows(LoginFailedException.class, () -> {
			loginService.login("nonexistent@example.com", "password");
		});

		assertEquals("아이디 또는 비밀번호가 맞지 않습니다.", exception.getMessage());
	}

	@Test
	void testCreateSession() {
		// given
		when(request.getSession(true)).thenReturn(session);

		// when
		loginService.createSession(user, request);

		// then
		verify(session).setAttribute(SessionConst.LOGIN_EMAIL, user.getEmail());
		verify(session).setAttribute(SessionConst.LOGIN_NICKNAME, user.getNickname());
	}
}
