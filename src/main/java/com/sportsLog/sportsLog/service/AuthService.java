package com.sportsLog.sportsLog.service;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.sportsLog.sportsLog.entity.User;
import com.sportsLog.sportsLog.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;

	public User login(String email, String password) {
		Optional<User> user = userRepository.findByEmail(email);
		if (user.isPresent() && BCrypt.checkpw(password, user.get().getPassword())) {
			return user.get();
		}
		return null;
	}

	public void logout(HttpSession session) {
		session.invalidate();
	}
}
