package com.sportsLog.sportsLog.service.auth;

import org.springframework.stereotype.Service;

import com.sportsLog.sportsLog.exception.LogoutFailedException;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogoutService {

	public void logout(HttpSession session) {
		if (session == null) {
			throw new LogoutFailedException("세션이 존재하지 않습니다.");
		}
		session.invalidate();
	}
}
