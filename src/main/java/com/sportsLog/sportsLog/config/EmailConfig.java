package com.sportsLog.sportsLog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    //JAVA MAILSENDER 인터페이스를 구현한 객체를 빈으로 등록하기 위함.
    @Bean
    public JavaMailSender mailSender() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("thisyoon97@gmail.com");
        mailSender.setPassword("qjmg hixa dhxs ahhx");

        Properties javaMailProperties = new Properties(); //JavaMail의 속성을 설정하기 위해 Properties 객체를 생성
        javaMailProperties.put("mail.transport.protocol", "smtp");
        javaMailProperties.put("mail.smtp.auth", "true");
        javaMailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        javaMailProperties.put("mail.smtp.starttls.enable", "true");
        javaMailProperties.put("mail.debug", "true");
        javaMailProperties.put("mail.smtp.ssl.trust", "smtp.naver.com");
        javaMailProperties.put("mail.smtp.ssl.protocols", "TLSv1.2");

        mailSender.setJavaMailProperties(javaMailProperties); //mailSender에 우리가 만든 properties 넣고
        return mailSender; //빈으로 등록한다.
    }
}
