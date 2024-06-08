package com.sportsLog.sportsLog.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportsLog.sportsLog.dto.AddUserRequestDto;
import com.sportsLog.sportsLog.service.UserService;

@WebMvcTest(UserController.class)
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private UserService userService;

	private AddUserRequestDto addUserRequestDto;

	@BeforeEach
	void setUp() {
		addUserRequestDto = new AddUserRequestDto();
		addUserRequestDto.setEmail("test@example.com");
		addUserRequestDto.setPassword("password123!");
		addUserRequestDto.setConfirmPassword("password123!");
		addUserRequestDto.setBirthdate(LocalDate.parse("1990-01-01"));
		addUserRequestDto.setAuthNumber("123456");
	}

	@Test
	void testShowSignupForm() throws Exception {
		mockMvc.perform(get("/user/signup"))
			.andExpect(status().isOk())
			.andExpect(view().name("user/signup"));
	}

	@Test
	void testSaveUserSuccess() throws Exception {
		mockMvc.perform(post("/user/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(addUserRequestDto)))
			.andExpect(status().isOk());
	}

	@Test
	void testSaveUserFailure() throws Exception {
		addUserRequestDto.setPassword("invalidPassword");
		mockMvc.perform(post("/user/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(addUserRequestDto)))
			.andExpect(status().isBadRequest());
	}

	@Test
	void testValidateMailDuplicationSuccess() throws Exception {
		mockMvc.perform(post("/mailDuplicationValidation")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"email\":\"test@example.com\"}"))
			.andExpect(status().isOk());
	}

	@Test
	void testValidateMailDuplicationFailure() throws Exception {
		String duplicateEmail = "duplicate@example.com";
		when(userService.checkMailDuplication(duplicateEmail)).thenReturn(true);

		mockMvc.perform(post("/mailDuplicationValidation")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"email\":\"" + duplicateEmail + "\"}"))
			.andExpect(status().isConflict());
	}
}