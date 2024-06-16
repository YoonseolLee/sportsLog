package com.sportsLog.sportsLog.controller.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindingResult;

import com.sportsLog.sportsLog.dto.LoginDto;
import com.sportsLog.sportsLog.entity.User;
import com.sportsLog.sportsLog.service.AuthService;
import com.sportsLog.sportsLog.validator.LoginValidator;

@SpringBootTest
class LoginControllerTest {

	@Autowired
	private LoginController loginController;

	@MockBean
	private AuthService authService;

	@MockBean
	private LoginValidator loginValidator;

	private BindingResult bindingResult;
	private MockHttpServletRequest request;

	@BeforeEach
	void setUp() {
		bindingResult = mock(BindingResult.class);
		request = new MockHttpServletRequest();
	}

	@Test
	void testLoginSuccessful() {
		// given
		LoginDto loginDto = new LoginDto("test@example.com", "password");
		User user = User.builder()
			.email("test@email.com")
			.password("password")
			.birthdate(LocalDate.of(1990, 5, 5))
			.role("USER")
			.nickname("pastaman")
			.emailVerified(true)
			.loginFailureCount(0)
			.passwordChangeDatetime(LocalDateTime.now())
			.lastLoginDatetime(LocalDateTime.now())
			.accountCreatedDateTime(LocalDateTime.now())
			.accountDeletedDateTime(null)
			.isAccountDeleted(false)
			.build();
		when(authService.login(loginDto.getEmail(), loginDto.getPassword())).thenReturn(user);

		// when
		ResponseEntity<Map<String, String>> response = loginController.login(loginDto, bindingResult, request);

		// then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		Map<String, String> body = response.getBody();
		assertEquals("/", body.get("redirectURL"));
	}

	@Test
	void testLoginFailedWithInvalidCredentials() {
		// Given
		LoginDto loginDto = new LoginDto("test@example.com", "wrongpassword");
		when(authService.login(loginDto.getEmail(), loginDto.getPassword())).thenReturn(null);

		// When
		ResponseEntity<Map<String, String>> response = loginController.login(loginDto, bindingResult, request);

		// Then
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		Map<String, String> body = response.getBody();
		assertEquals("아이디 또는 비밀번호가 맞지 않습니다.", body.get("loginFail"));
	}

	@Test
	void testLoginFailedWithValidationErrors() {
		// given
		LoginDto loginDto = new LoginDto("", "");
		when(bindingResult.hasErrors()).thenReturn(true);

		// when
		ResponseEntity<Map<String, String>> response = loginController.login(loginDto, bindingResult, request);

		// then
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		verify(bindingResult, times(1)).getFieldErrors();
		verify(bindingResult, times(1)).getGlobalErrors();
	}

}