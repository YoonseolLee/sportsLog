package com.sportsLog.sportsLog.service.auth;

import org.springframework.stereotype.Service;

import com.sportsLog.sportsLog.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogoutService {

	private final UserRepository userRepository;

	public void logout(HttpSession session) {
		session.invalidate();
	}
}
