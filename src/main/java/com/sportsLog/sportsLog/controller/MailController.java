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
    // 선언부: 클라이언트가 보낸 데이터를 EmailCheckDto 객체로 변환하고, 유효성 검사를 수행한다.
    public ResponseEntity<String> AuthCheck(@RequestBody @Valid EmailCheckDto emailCheckDto) {
        Boolean Checked = mailService.CheckAuthNum(emailCheckDto.getEmail(), emailCheckDto.getAuthNum());
        if (Checked) {
            return ResponseEntity.ok("ok");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증 실패");
        }
    }
}
