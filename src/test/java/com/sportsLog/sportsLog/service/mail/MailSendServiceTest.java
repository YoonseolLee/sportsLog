package com.sportsLog.sportsLog.service.mail;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

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
    public void testMailSend() throws MessagingException {
        String setFrom = "from@example.com";
        String toMail = "to@example.com";
        String title = "테스트 메일";
        String content = "테스트입니다.";
        int authNumber = mailSendService.getAuthNumber();

        // mock MimeMessage
        MimeMessage mimeMessage = mock(MimeMessage.class);
        given(mailSender.createMimeMessage()).willReturn(mimeMessage);

        // mock redisUtil.setDataExpire()
        doNothing().when(redisUtil).setDataExpire(toMail, Integer.toString(authNumber), 60 * 5L);

        mailSendService.mailSend(setFrom, toMail, title, content);

        // Assertions
        // 각 메소드가 한 번씩 호출되었는지 검증
        verify(mailSender).createMimeMessage();
        verify(mailSender).send(mimeMessage);
        verify(redisUtil).setDataExpire(toMail, Integer.toString(authNumber), 60 * 5L);
    }

    @Test
    public void testCheckAuthNum_Success() {
        String email = "test@example.com";
        String authNum = "123456";

        given(redisUtil.getData(email)).willReturn(authNum);

        boolean isAuthNumValid = mailSendService.checkAuthNum(email, authNum);
        assertTrue(isAuthNumValid);
    }

    @Test
    public void testCheckAuthNum_Failure() {
        String email = "test@example.com";
        String authNum = "123456";

        given(redisUtil.getData(email)).willReturn("111111");

        boolean isAuthNumValid = mailSendService.checkAuthNum(email, authNum);
        assertFalse(isAuthNumValid);
    }
}