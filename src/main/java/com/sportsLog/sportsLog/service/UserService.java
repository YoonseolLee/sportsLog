package com.sportsLog.sportsLog.service;

import com.sportsLog.sportsLog.dto.AddUserRequestDto;
import com.sportsLog.sportsLog.entity.User;
import com.sportsLog.sportsLog.service.mail.MailSendService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final MailSendService mailSendService;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Long addUser(AddUserRequestDto addUserRequestDto) {

        log.info("authNum = {}, ", addUserRequestDto.getAuthNumber());
        log.info("confirmPassword = {}, ", addUserRequestDto.getConfirmPassword());


        boolean isValid = isSignupFormInputValid(addUserRequestDto);

        if (isValid) {
            log.info("이메일은 이거다:{}", addUserRequestDto.getEmail());
            log.info("birthdate는 이거다:{}", addUserRequestDto.getBirthdate());
            log.info("비번은 이거다:{}", addUserRequestDto.getPassword());
            String encodedPassword = BCrypt.hashpw(addUserRequestDto.getPassword(), BCrypt.gensalt());

            User user = User.builder()
                    .email(addUserRequestDto.getEmail())
                    .password(encodedPassword)
                    .birthdate(addUserRequestDto.getBirthdate())
                    .emailVerified(true)
                    .passwordChangeDatetime(LocalDateTime.now())
                    .lastLoginDatetime(LocalDateTime.now())
                    .accountCreatedDateTime(LocalDateTime.now())
                    .accountDeletedDateTime(null)
                    .isAccountDeleted(false)
                    .build();

            entityManager.persist(user);
            return user.getId();
        } else {
            throw new IllegalArgumentException("입력된 값이 유효하지 않습니다.");
        }
    }

    @Transactional
    public boolean isSignupFormInputValid(@RequestBody @Validated AddUserRequestDto addUserRequestDto) {
        boolean isEmailVerified = mailSendService.checkAuthNum(addUserRequestDto.getEmail(), addUserRequestDto.getAuthNumber());
        if (!isEmailVerified) {
            return false;
        }

        if (!addUserRequestDto.getPassword().equals(addUserRequestDto.getConfirmPassword())) {
            return false;
        }
        log.info("isSignupFormInputValid 통과");
        return true;
    }
}
