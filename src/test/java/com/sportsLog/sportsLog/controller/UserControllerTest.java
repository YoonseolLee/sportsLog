package com.sportsLog.sportsLog.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sportsLog.sportsLog.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserControllerTest.class)
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

//  @Test
//  void showSignupForm() {
//    mockMvc.perform(get("/user/signup"))
//        .andExpect(status().isOk())
//        .andExpect(view().name("/user/signup"));
//  }

  @Test
  void saveUser() throws Exception {
    mockMvc.perform(post("/user/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .param("email", "test@example.com")
            .param("authNumber", "123456")
            .param("password", "Password123@")
            .param("confirmPassword", "Password123@")
            .param("birthdate", "1990-01-01"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/user/signup"));
  }

  @Test
  void validateMailDuplication() {
  }
}