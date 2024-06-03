package com.sportsLog.sportsLog.controller;

import com.sportsLog.sportsLog.dto.mail.EmailCheckDto;
import com.sportsLog.sportsLog.dto.mail.EmailRequestDto;
import com.sportsLog.sportsLog.service.mail.MailSendService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.mockito.BDDMockito.given;


@WebMvcTest(MailController.class)
class MailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MailSendService mailSendService;

    @Test
    public void testMailSend() throws Exception {
        String email = "test@example.com";
        String authNum = "!a123456";

        EmailRequestDto emailRequestDto = new EmailRequestDto();
        emailRequestDto.setEmail(email);

        given(mailSendService.joinEmail(email)).willReturn(authNum);

        mockMvc.perform(post("/mailSend")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"" + email + "\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(authNum));
    }

    @Test
    public void testAuthCheck_Success() throws Exception {
        String email = "test@example.com";
        String authNum = "!a123456";

        EmailCheckDto emailCheckDto = new EmailCheckDto();
        emailCheckDto.setEmail(email);
        emailCheckDto.setAuthNum(authNum);

        given(mailSendService.checkAuthNum(email, authNum)).willReturn(true);

        mockMvc.perform(post("/mailauthCheck")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"" + email + "\", \"authNum\": \"" + authNum + "\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testAuthCheck_Fail() throws Exception {
        String email = "test@example.com";
        String authNum = "!a123456";

        EmailCheckDto emailCheckDto = new EmailCheckDto();
        emailCheckDto.setEmail(email);
        emailCheckDto.setAuthNum(authNum);

        given(mailSendService.checkAuthNum(email, authNum)).willReturn(false);

        mockMvc.perform(post("/mailauthCheck")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"" + email + "\", \"authNum\": \"" + authNum + "\"}"))
                .andExpect(status().isBadRequest());
    }
}