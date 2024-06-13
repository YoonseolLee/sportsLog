package com.sportsLog.sportsLog.service.auth;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.sportsLog.sportsLog.entity.User;
import com.sportsLog.sportsLog.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService {

	private final UserRepository userRepository;

	public User login(String email, String password) {
		Optional<User> user = userRepository.findByEmail(email);
		if (user.isPresent() && BCrypt.checkpw(password, user.get().getPassword())) {
			return user.get();
		}
		return null;
	}
}
