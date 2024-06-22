package com.sportsLog.sportsLog.controller.auth;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sportsLog.sportsLog.dto.AddUserRequestDto;
import com.sportsLog.sportsLog.exception.SignupFailedException;
import com.sportsLog.sportsLog.service.auth.SignupService;

@WebMvcTest(SignupController.class)
class SignupControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SignupService signupService;

	private ObjectMapper objectMapper;

	private AddUserRequestDto validUserDto;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());

		validUserDto = new AddUserRequestDto();
		validUserDto.setEmail("test@example.com");
		validUserDto.setAuthNumber("123456");
		validUserDto.setPassword("Password1!");
		validUserDto.setConfirmPassword("Password1!");
		validUserDto.setNickname("testuser");
		validUserDto.setBirthdate(LocalDate.of(2000, 1, 1));
		validUserDto.setEmailVerified(true);
	}

	@Test
	void showSignupForm_ShouldReturnSignupFormView() throws Exception {
		// given

		// when
		MvcResult result = mockMvc.perform(get("/auth/signup"))
			.andExpect(status().isOk())
			.andExpect(view().name("auth/signup"))
			.andReturn();

		// then
		String viewName = result.getModelAndView().getViewName();
		assertThat(viewName).isEqualTo("auth/signup");
	}

	@Test
	void saveUser_ShouldReturnBadRequest_WhenValidationFails() throws Exception {
		// given
		AddUserRequestDto invalidUserDto = new AddUserRequestDto();
		// invalid data

		// when
		MvcResult result = mockMvc.perform(post("/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalidUserDto)))
			.andExpect(status().isBadRequest())
			.andReturn();

		// then
		String response = result.getResponse().getContentAsString();

		String expectedEmailError = "이메일은 필수 입력 값입니다.";
		String expectedAuthNumberError = "인증 번호는 필수 입력 값입니다.";
		String expectedPasswordError = "비밀번호는 필수 입력 값입니다.";
		String expectedConfirmPasswordError = "비밀번호 확인은 필수 입력 값입니다.";
		String expectedNicknameError = "닉네임은 필수 입력 값입니다.";
		String expectedBirthdateError = "생년월일은 필수 입력 값입니다.";

		assertThat(response).contains(expectedEmailError);
		assertThat(response).contains(expectedAuthNumberError);
		assertThat(response).contains(expectedPasswordError);
		assertThat(response).contains(expectedConfirmPasswordError);
		assertThat(response).contains(expectedNicknameError);
		assertThat(response).contains(expectedBirthdateError);
	}

	@Test
	void saveUser_Success() throws Exception {
		// given
		when(signupService.addUser(any(AddUserRequestDto.class))).thenReturn(1L);

		// when
		MvcResult result = mockMvc.perform(post("/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(validUserDto)))
			.andExpect(status().isOk())
			.andReturn();

		// then
		String response = result.getResponse().getContentAsString();
		assertThat(response).isEqualTo("회원가입에 성공하였습니다.");
	}

	@Test
	void saveUser_ShouldReturnBadRequest_WhenSignupFails() throws Exception {
		// given
		AddUserRequestDto requestDto = new AddUserRequestDto();
		requestDto.setEmail("test@example.com");
		requestDto.setAuthNumber("123456");
		requestDto.setPassword("Password1!");
		requestDto.setConfirmPassword("Password1!");
		requestDto.setNickname("testuser");
		requestDto.setBirthdate(LocalDate.of(2000, 1, 1));
		requestDto.setEmailVerified(true);

		when(signupService.addUser(any(AddUserRequestDto.class))).thenThrow(new SignupFailedException("Signup failed"));

		// when
		MvcResult result = mockMvc.perform(post("/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestDto)))
			.andExpect(status().isBadRequest())
			.andReturn();

		// then
		String response = result.getResponse().getContentAsString();
		assertThat(response).isEqualTo("Signup failed");
	}

	@Test
	void validateMailDuplication_ShouldReturnConflict_WhenEmailDuplicated() throws Exception {
		// given
		Map<String, String> request = new HashMap<>();
		request.put("email", "test@example.com");

		when(signupService.checkMailDuplication("test@example.com")).thenReturn(true);

		// when
		MvcResult result = mockMvc.perform(post("/auth/mailDuplicationValidation")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isConflict())
			.andReturn();

		// then
		String response = result.getResponse().getContentAsString();
		assertThat(response).isEqualTo("이미 가입된 이메일 주소입니다.");
	}

	@Test
	void validateMailDuplication_ShouldReturnOk_WhenEmailNotDuplicated() throws Exception {
		// given
		Map<String, String> request = new HashMap<>();
		request.put("email", "test@example.com");

		when(signupService.checkMailDuplication("test@example.com")).thenReturn(false);

		// when
		MvcResult result = mockMvc.perform(post("/auth/mailDuplicationValidation")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andReturn();

		// then
		String response = result.getResponse().getContentAsString();
		assertThat(response).isEqualTo("가입할 수 있는 이메일 주소입니다.");
	}

	@Test
	void validateNicknameDuplication_ShouldReturnConflict_WhenNicknameDuplicated() throws Exception {
		// given
		Map<String, String> request = new HashMap<>();
		request.put("nickname", "testuser");

		when(signupService.checkNicknameDuplication("testuser")).thenReturn(true);

		// when
		MvcResult result = mockMvc.perform(post("/auth/nicknameDuplicationValidation")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isConflict())
			.andReturn();

		// then
		String response = result.getResponse().getContentAsString();
		assertThat(response).isEqualTo("이미 사용 중인 닉네임입니다.");
	}

	@Test
	void validateNicknameDuplication_ShouldReturnOk_WhenNicknameNotDuplicated() throws Exception {
		// given
		Map<String, String> request = new HashMap<>();
		request.put("nickname", "testuser");

		when(signupService.checkNicknameDuplication("testuser")).thenReturn(false);

		// when
		MvcResult result = mockMvc.perform(post("/auth/nicknameDuplicationValidation")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andReturn();

		// then
		String response = result.getResponse().getContentAsString();
		assertThat(response).isEqualTo("사용 가능한 닉네임입니다.");
	}
}

