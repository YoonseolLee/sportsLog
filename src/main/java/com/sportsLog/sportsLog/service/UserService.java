package com.sportsLog.sportsLog.service;

import com.sportsLog.sportsLog.dto.AddUserRequestDto;
import com.sportsLog.sportsLog.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Long saveUser(AddUserRequestDto addUserRequestDto) {
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
