package com.sportsLog.sportsLog.controller.auth;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportsLog.sportsLog.dto.LoginDto;
import com.sportsLog.sportsLog.entity.User;
import com.sportsLog.sportsLog.service.auth.LoginService;

@WebMvcTest(LoginController.class)
class LoginControllerTest {

	// TODO: 에러발생.. 수정 필요!!

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private LoginService loginService;

	@BeforeEach
	void setUp(WebApplicationContext webApplicationContext) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	void testLoginSuccess() throws Exception {
		// given
		LoginDto loginDto = new LoginDto();
		loginDto.setEmail("test@example.com");
		loginDto.setPassword("ValidPassword123!"); // Valid password

		User user = new User();
		Mockito.when(loginService.login(anyString(), anyString())).thenReturn(user);

		// when
		MvcResult result = mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginDto)))
			.andExpect(status().isOk())
			.andReturn();

		// then
		String jsonResponse = result.getResponse().getContentAsString();
		Map<String, String> responseMap = objectMapper.readValue(jsonResponse, Map.class);

		assertThat(responseMap).containsEntry("redirectURL", "/");
	}

	@Test
	void testLoginFail_BeanValidation_Fail() throws Exception {
		// given
		LoginDto loginDto = new LoginDto();
		loginDto.setEmail("test@example.co");
		loginDto.setPassword("short"); // Invalid password

		// when & then
		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginDto)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.password").value("비밀번호는 영문, 숫자, 특수문자를 포함하여 8~20자리여야 합니다."));
	}

	@Test
	void testLoginFail_UserNotFound() throws Exception {
		// given
		LoginDto loginDto = new LoginDto();
		loginDto.setEmail("nonexistent@example.com");
		loginDto.setPassword("ValidPassword123!");

		Mockito.when(loginService.login(anyString(), anyString()))
			.thenReturn(null); // 사용자가 존재하지 않음을 모의

		// when & then
		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginDto)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.error").value("User not found"));
	}

	@Test
	void testLoginFail_PasswordMatchFail() throws Exception {
		// given
		LoginDto loginDto = new LoginDto();
		loginDto.setEmail("test@example.com");
		loginDto.setPassword("WrongPassword");

		User user = new User();
		user.setPassword("CorrectPassword"); // 모의 사용자의 비밀번호 설정
		Mockito.when(loginService.login(anyString(), anyString()))
			.thenReturn(user);

		// when & then
		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginDto)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.error").value("Invalid password"));
	}
}