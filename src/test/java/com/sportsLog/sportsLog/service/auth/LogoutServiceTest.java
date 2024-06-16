package com.sportsLog.sportsLog.service.auth;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;

import com.sportsLog.sportsLog.exception.LogoutFailedException;

import jakarta.servlet.http.HttpSession;

@SpringBootTest
class LogoutServiceTest {

	private LogoutService logoutService;

	@BeforeEach
	void setUp() {
		logoutService = new LogoutService(); // UserRepository는 사용되지 않으므로 null로 설정
	}

	@Test
	void testLogoutSuccessful() {
		// given
		HttpSession session = new MockHttpSession();

		// when
		logoutService.logout(session);

		// then
		assertThrows(IllegalStateException.class, session::invalidate); // 세션이 이미 무효화되었는지 확인
	}

	@Test
	void testLogoutFailedWithNoSession() {
		// given
		HttpSession session = null;

		// when / then
		LogoutFailedException exception = assertThrows(LogoutFailedException.class, () -> {
			logoutService.logout(session);
		});

		assertEquals("세션이 존재하지 않습니다.", exception.getMessage());
	}
}