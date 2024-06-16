package com.sportsLog.sportsLog.service.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sportsLog.sportsLog.entity.User;
import com.sportsLog.sportsLog.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private LoginService loginService;

	@Test
	void testLoginSuccessful() {
		// given
		String email = "test@example.com";
		String password = "!test123456";
		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		User user = new User(1L, email, hashedPassword, null, null, null, false, 0, null, null, null, null, false);
		when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

		// when
		User loggedInUser = loginService.login(email, password);

		// then
		assertEquals(user, loggedInUser);
	}

	@Test
	void testLoginFailedWithInvalidEmail() {
		// given
		String email = "invalid@example.com";
		String password = "!test123456";
		when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

		// when
		User loggedInUser = loginService.login(email, password);

		// then
		assertNull(loggedInUser);
	}

	@Test
	void testLoginFailedWithInvalidPassword() {
		// given
		String email = "test@example.com";
		String password = "wrongpassword";
		String hashedPassword = BCrypt.hashpw("password", BCrypt.gensalt());
		User user = new User(1L, email, hashedPassword, null, null, null, false, 0, null, null, null, null, false);
		when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

		// when
		User loggedInUser = loginService.login(email, password);

		// then
		assertNull(loggedInUser);
	}
}