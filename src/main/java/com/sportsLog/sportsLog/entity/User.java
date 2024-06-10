package com.sportsLog.sportsLog.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDate birthdate;

    @Column(nullable = false)
    private String role;

    private boolean emailVerified;
    private int loginFailureCount;
    private LocalDateTime passwordChangeDatetime;
    private LocalDateTime lastLoginDatetime;
    private LocalDateTime accountCreatedDateTime;
    private LocalDateTime accountDeletedDateTime;
    private boolean isAccountDeleted;

    @Builder
    public static User createUser(String email, String password, LocalDate birthdate, String role,
        boolean emailVerified, int loginFailureCount, LocalDateTime passwordChangeDatetime,
        LocalDateTime lastLoginDatetime, LocalDateTime accountCreatedDateTime,
        LocalDateTime accountDeletedDateTime, boolean isAccountDeleted) {
        User user = new User();
        user.email = email;
        user.password = password;
        user.birthdate = birthdate;
        user.role = role;
        user.emailVerified = emailVerified;
        user.loginFailureCount = loginFailureCount;
        user.passwordChangeDatetime = passwordChangeDatetime;
        user.lastLoginDatetime = lastLoginDatetime;
        user.accountCreatedDateTime = accountCreatedDateTime;
        user.accountDeletedDateTime = accountDeletedDateTime;
        user.isAccountDeleted = isAccountDeleted;
        return user;
    }
}
