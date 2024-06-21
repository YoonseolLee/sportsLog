package com.sportsLog.sportsLog.controller.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

import com.sportsLog.sportsLog.exception.LogoutFailedException;
import com.sportsLog.sportsLog.service.auth.LogoutService;

@ExtendWith(MockitoExtension.class)
class LogoutControllerTest {

	@InjectMocks
	private LogoutController logoutController;

	@Mock
	private LogoutService logoutService;

	@Test
	void logout_ValidSession_ShouldRedirectToSpecifiedUrl() {
		// given
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpSession session = new MockHttpSession();
		request.setSession(session);
		String redirectUrl = "/main";

		// when
		String result = logoutController.logout(request, redirectUrl);

		// then
		verify(logoutService, times(1)).logout(session);
		assertEquals("redirect:/main", result);
	}

	@Test
	void logout_SessionIsNull_ShouldThrowLogoutFailedException() {
		// given
		// session을 request에 넣지 않아서 자동으로 null이 입력됨
		MockHttpServletRequest request = new MockHttpServletRequest();
		String redirectUrl = "/main";

		doThrow(new LogoutFailedException("세션이 존재하지 않습니다."))
			.when(logoutService).logout(null);

		// when, then
		assertThrows(LogoutFailedException.class, () -> {
			logoutController.logout(request, redirectUrl);
		});

		verify(logoutService, times(1)).logout(null);
	}
}