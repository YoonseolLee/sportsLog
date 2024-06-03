package com.sportsLog.sportsLog.controller;

import com.sportsLog.sportsLog.dto.mail.EmailCheckDto;
import com.sportsLog.sportsLog.dto.mail.EmailRequestDto;
import com.sportsLog.sportsLog.service.mail.MailSendService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MailController {

    private final MailSendService mailService;

    /**
     * 메일을 전송하는 메서드
     */
    @PostMapping("/mailSend")
    public String mailSend(@RequestBody @Valid EmailRequestDto emailDto) {
        return mailService.joinEmail(emailDto.getEmail());
    }

    @PostMapping("/mailauthCheck")
    public ResponseEntity<Void> authCheck(@RequestBody @Valid EmailCheckDto emailCheckDto) {
        Boolean checked = mailService.checkAuthNum(emailCheckDto.getEmail(), emailCheckDto.getAuthNum());
        if (checked) {
            log.info("메일 인증 성공");
            return ResponseEntity.ok().build(); // HTTP 200 OK
        }
        if (!checked) {
            log.info("메일 인증 실패");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // HTTP 400 Bad Request
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
