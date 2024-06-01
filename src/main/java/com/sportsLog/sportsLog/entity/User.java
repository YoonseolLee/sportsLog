package com.sportsLog.sportsLog.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;
    private LocalDate birthdate;
    private boolean emailVerified;

    private int loginFailureCount;
    private LocalDateTime passwordChangeDatetime;
    private LocalDateTime lastLoginDatetime;
    private LocalDateTime accountCreatedDateTime;
    private LocalDateTime accountDeletedDateTime;
    private boolean isAccountDeleted;
}


