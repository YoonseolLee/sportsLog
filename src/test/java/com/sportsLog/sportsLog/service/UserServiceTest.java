package com.sportsLog.sportsLog.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sportsLog.sportsLog.dto.AddUserRequestDto;
import com.sportsLog.sportsLog.entity.User;
import com.sportsLog.sportsLog.repository.UserRepository;
import com.sportsLog.sportsLog.service.User.UserService;
import com.sportsLog.sportsLog.service.mail.MailSendService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@Mock
	private MailSendService mailSendService;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService;

	private AddUserRequestDto validRequestDto;
	private AddUserRequestDto invalidRequestDto;

	@BeforeEach
	public void setUp() {
		validRequestDto = AddUserRequestDto.builder()
			.email("test@example.com")
			.password("password")
			.confirmPassword("password")
			.birthdate(LocalDate.now().minusYears(20))
			.authNumber("123456")
			.build();

		invalidRequestDto = AddUserRequestDto.builder()
			.email("test@example.com")
			.password("password")
			.confirmPassword("wrongpassword")
			.birthdate(LocalDate.now().minusYears(20))
			.authNumber("123456")
			.build();
	}

	// @Test
	// public void addUser_whenValidInput_thenSavesUser() {
	// 	when(mailSendService.checkAuthNum(anyString(), anyString())).thenReturn(true);
	// 	doNothing().when(userRepository).save(any(User.class));
	//
	// 	// Mock the behavior of setting an ID after saving
	// 	when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
	// 		User user = invocation.getArgument(0);
	// 		user.setId(1L);
	// 		return user;
	// 	});
	//
	// 	Long userId = userService.addUser(validRequestDto);
	//
	// 	assertNotNull(userId);
	// 	verify(userRepository, times(1)).save(any(User.class));
	// }

	@Test
	public void addUser_whenInvalidInput_thenThrowsException() {
		when(mailSendService.checkAuthNum(anyString(), anyString())).thenReturn(true);

		assertThrows(IllegalArgumentException.class, () -> {
			userService.addUser(invalidRequestDto);
		});

		verify(userRepository, times(0)).save(any(User.class));
	}

	@Test
	public void isSignupFormInputValid_whenEmailVerifiedAndPasswordsMatch_thenReturnsTrue() {
		when(mailSendService.checkAuthNum(anyString(), anyString())).thenReturn(true);

		boolean isValid = userService.isSignupFormInputValid(validRequestDto);

		assertTrue(isValid);
	}

	@Test
	public void isSignupFormInputValid_whenEmailNotVerified_thenReturnsFalse() {
		when(mailSendService.checkAuthNum(anyString(), anyString())).thenReturn(false);

		boolean isValid = userService.isSignupFormInputValid(validRequestDto);

		assertFalse(isValid);
	}

	@Test
	public void isSignupFormInputValid_whenPasswordsDoNotMatch_thenReturnsFalse() {
		when(mailSendService.checkAuthNum(anyString(), anyString())).thenReturn(true);

		boolean isValid = userService.isSignupFormInputValid(invalidRequestDto);

		assertFalse(isValid);
	}

	@Test
	public void checkMailDuplication_whenEmailExists_thenReturnsTrue() {
		when(userRepository.countByEmail(anyString())).thenReturn(1L);

		boolean isDuplicated = userService.checkMailDuplication("test@example.com");

		assertTrue(isDuplicated);
	}

	@Test
	public void checkMailDuplication_whenEmailDoesNotExist_thenReturnsFalse() {
		when(userRepository.countByEmail(anyString())).thenReturn(0L);

		boolean isDuplicated = userService.checkMailDuplication("test@example.com");

		assertFalse(isDuplicated);
	}
}
