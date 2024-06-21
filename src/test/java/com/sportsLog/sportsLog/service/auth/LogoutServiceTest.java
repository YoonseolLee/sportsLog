package com.sportsLog.sportsLog.service.auth;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

import com.sportsLog.sportsLog.exception.LogoutFailedException;

@ExtendWith(MockitoExtension.class)
class LogoutServiceTest {

	private LogoutService logoutService;

	@BeforeEach
	void setUp() {
		logoutService = new LogoutService();
	}

	@Test
	void logout_WithValidSession_ShouldInvalidateSession() {
		// given
		MockHttpSession session = new MockHttpSession();

		// when
		logoutService.logout(session);

		// then
		assertTrue(session.isInvalid());
	}

	@Test
	void logout_WithNonExistentSession_ShouldThrowLogoutFailedException() {
		// Given
		MockHttpServletRequest request = new MockHttpServletRequest();
		// 세션을 설정하지 않음

		// When & Then
		LogoutFailedException exception = assertThrows(LogoutFailedException.class,
			() -> logoutService.logout(request.getSession(false)));

		assertEquals("세션이 존재하지 않습니다.", exception.getMessage());
	}

}