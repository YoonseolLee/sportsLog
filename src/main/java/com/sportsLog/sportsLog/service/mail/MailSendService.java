package com.sportsLog.sportsLog.service.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@Slf4j
public class MailSendService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private RedisUtil redisUtil;
    private int authNumber;

    // 랜덤 6자리 양수를 반환한다.
    public void makeRandomNumber() {
        Random r = new Random();
        String randomNumber = "";
        for (int i = 0; i < 6; i++) {
            randomNumber += Integer.toString(r.nextInt(10));
        }
        authNumber = Integer.parseInt(randomNumber);
    }

    //메일을 어디서 보내는지, 어디로 보내는지, 인증 번호를 html 형식으로 어떻게 보내는지를 작성
    public String joinEmail(String email) {
        makeRandomNumber();
        String setForm = "thisyoon97@gmail.com";
        String toMail = email;
        String title = "회원 가입 인증 이메일 입니다."; // 이메일 제목
        String content =
                "저희 게시판에 방문해주셔서 감사합니다." +
                        "<br><br>" +
                        "인증 번호는 " + authNumber + "입니다. " +
                        "<br>" +
                        "인증번호를 입력해주세요.";
        mailSend(setForm, toMail, title, content);
        return Integer.toString(authNumber);
    }

    public void mailSend(String setFrom, String toMail, String title, String content) {
        MimeMessage message = mailSender.createMimeMessage(); //JavaMailSender 객체를 사용하여 MimeMessage 객체를 생성
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            // true를 전달하여 multipart 형식의 메시지를 지원하고, "utf-8"을 전달하여 문자 인코딩을 설정
            helper.setFrom(setFrom);//이메일의 발신자 주소 설정
            helper.setTo(toMail);//이메일의 수신자 주소 설정
            helper.setSubject(title);//이메일의 제목을 설정
            helper.setText(content, true);//이메일의 내용 설정 두 번째 매개 변수에 true를 설정하여 html 설정으로한다.
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        // 5분 동안 인증번호가 살게 한다.
        redisUtil.setDataExpire(Integer.toString(authNumber), toMail, 60 * 5L);
    }

    /**
     * 사용자가 입력한 인증번호와 실제 인증번호를 비교한다.
     */
    public Boolean CheckAuthNum(String email, String authNum) {
        if (redisUtil.getData(authNum) == null) {
            log.info("인증 실패: {}");
            return false;
        }
        if (redisUtil.getData(authNum).equals(email)) {
            log.info("인증 성공: {}", authNum);
            return true;
        } else {
            log.info("인증 실패: {}", authNum);
            return false;
        }
    }
}
