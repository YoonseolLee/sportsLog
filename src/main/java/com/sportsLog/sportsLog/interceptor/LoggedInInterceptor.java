package com.sportsLog.sportsLog.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.sportsLog.sportsLog.common.SessionConst;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LoggedInInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
		Object handler) throws Exception {
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute(SessionConst.LOGIN_EMAIL) != null) {
			log.info("이미 로그인된 사용자 요청 차단: {}", request.getRequestURI());

			response.sendRedirect("/");
			return false;
		}
		return true;
	}
}
