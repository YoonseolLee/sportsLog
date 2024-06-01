package com.sportsLog.sportsLog.service;

import com.sportsLog.sportsLog.dto.AddUserRequestDto;
import com.sportsLog.sportsLog.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Long addUser(AddUserRequestDto addUserRequestDto) {
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
    }
}
