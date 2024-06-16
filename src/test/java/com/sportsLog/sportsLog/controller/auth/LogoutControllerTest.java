package com.sportsLog.sportsLog.controller.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

import com.sportsLog.sportsLog.exception.LogoutFailedException;
import com.sportsLog.sportsLog.service.auth.LogoutService;

@SpringBootTest
class LogoutControllerTest {

	@Autowired
	private LogoutController logoutController;

	@MockBean
	private LogoutService logoutService;

	private MockHttpServletRequest request;
	private MockHttpSession session;

	@BeforeEach
	void setUp() {
		request = new MockHttpServletRequest();
		session = new MockHttpSession();
	}

	@Test
	void testLogoutSuccessful() {
		// given
		request.setSession(session);

		// when
		ResponseEntity<Void> response = logoutController.logout(request);

		// then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(logoutService, times(1)).logout(session);
	}

	@Test
	void testLogoutFailedWithNoSession() {
		// given
		request.setSession(null);
		doThrow(new LogoutFailedException("세션이 존재하지 않습니다.")).when(logoutService).logout(null);

		// when / then
		LogoutFailedException exception = assertThrows(LogoutFailedException.class, () -> {
			logoutController.logout(request);
		});

		assertEquals("세션이 존재하지 않습니다.", exception.getMessage());
		verify(logoutService, times(1)).logout(null);
	}
}