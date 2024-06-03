package com.sportsLog.sportsLog.service.mail;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.BDDMockito.given;

class MailSendServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private RedisUtil redisUtil;

    @InjectMocks
    private MailSendService mailSendService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMakeRandomNumber() {
        for (int i = 0; i < 1000; i++) {
            mailSendService.makeRandomNumber();
            int authNumber = mailSendService.getAuthNumber();
            assertTrue(authNumber >= 100000 && authNumber <= 999999);
        }
    }

    @Test
    public void testJoinEmail() throws MessagingException {
        String email = "test@example.com";
        String authNum = "123456!a";

        // MimeMessage 객체를 Mocking
        MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);
        given(mailSender.createMimeMessage()).willReturn(mimeMessage);

        // RedisUtil 객체를 Mocking
        given(redisUtil.getData(email)).willReturn(authNum);

        // mailSender.send() 메서드를 Mocking
        doNothing().when(mailSender).send(mimeMessage);

        // 실제 서비스 메서드 호출
        String returnedAuthNum = mailSendService.joinEmail(email);

        // Assertions
        assertNotNull(returnedAuthNum);
        verify(mailSender).send(mimeMessage);
    }


    @Test
    public void testCheckAuthNum_Success() {
        String email = "test@example.com";
        String authNum = "123456!a";

        given(redisUtil.getData(email)).willReturn(authNum);

        boolean result = mailSendService.checkAuthNum(email, authNum);
        assertTrue(result);
    }
}