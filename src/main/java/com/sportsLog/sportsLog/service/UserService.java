package com.sportsLog.sportsLog.service;

import com.sportsLog.sportsLog.dto.AddUserRequestDto;
import com.sportsLog.sportsLog.entity.User;
import com.sportsLog.sportsLog.service.mail.MailSendService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
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
    private EntityManager em;

    @Transactional
    public Long addUser(AddUserRequestDto addUserRequestDto) {

        log.info("authNum = {}, ", addUserRequestDto.getAuthNumber());
        log.info("confirmPassword = {}, ", addUserRequestDto.getConfirmPassword());


        boolean isValid = isSignupFormInputValid(addUserRequestDto);
        // TODO: 이메일 중복여부 검증
        // TODO: 서버 검증 실패 시 alert
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

            em.persist(user);
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

    /**
     * DB에 동일한 email에 저장되어 있으면 true를 반환한다.
     * cf. 특정 조건을 만족하는 엔티티의 개수를 반환받아야 하므로 Long 타입을 사용한다.
     * @param email
     * @return
     */
    public boolean checkMailDuplication(String email) {
        String jpql = "SELECT COUNT(u) FROM User u WHERE u.email = :email";
        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        query.setParameter("email", email);

        try {
            Long count = query.getSingleResult();
            return count > 0;
        } catch (NoResultException e) {
            return false;
        }
    }
}
