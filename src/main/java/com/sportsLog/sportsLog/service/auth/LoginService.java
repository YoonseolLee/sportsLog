package com.sportsLog.sportsLog.service.auth;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.sportsLog.sportsLog.common.SessionConst;
import com.sportsLog.sportsLog.entity.User;
import com.sportsLog.sportsLog.exception.LoginFailedException;
import com.sportsLog.sportsLog.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService {

	private final UserRepository userRepository;

	public User login(String email, String password) {
		Optional<User> userOptional = userRepository.findByEmail(email);

		if (userOptional.isEmpty()) {
			throw new LoginFailedException("아이디 또는 비밀번호가 맞지 않습니다.");
		}
		if (!BCrypt.checkpw(password, userOptional.get().getPassword())) {
			throw new LoginFailedException("비밀번호가 일치하지 않습니다.");
		}
		return userOptional.get();
	}

	public void createSession(User user, HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		session.setAttribute(SessionConst.LOGIN_EMAIL, user.getEmail());
		session.setAttribute("nickname", user.getNickname());
	}
}
