package com.sportsLog.sportsLog.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UserStatus {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private boolean emailVerified;
	private int loginFailureCount;
	private LocalDateTime passwordChangeDatetime;
	private LocalDateTime lastLoginDatetime;
	private LocalDateTime accountCreatedDateTime;
	private LocalDateTime accountDeletedDateTime;
	private boolean isAccountDeleted;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Builder
	public static UserStatus createUserStatus(boolean emailVerified, int loginFailureCount,
		LocalDateTime passwordChangeDatetime, LocalDateTime lastLoginDatetime,
		LocalDateTime accountCreatedDateTime, LocalDateTime accountDeletedDateTime,
		boolean isAccountDeleted) {
		UserStatus userStatus = new UserStatus();
		userStatus.emailVerified = emailVerified;
		userStatus.loginFailureCount = loginFailureCount;
		userStatus.passwordChangeDatetime = passwordChangeDatetime;
		userStatus.lastLoginDatetime = lastLoginDatetime;
		userStatus.accountCreatedDateTime = accountCreatedDateTime;
		userStatus.accountDeletedDateTime = accountDeletedDateTime;
		userStatus.isAccountDeleted = isAccountDeleted;
		return userStatus;
	}
}
