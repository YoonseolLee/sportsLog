package com.sportsLog.sportsLog.interceptor;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {

	public static final String LOG_ID = "logId";
	public static final String START_TIME = "startTime";


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
		Exception {
		String requestURI = request.getRequestURI();
		String uuid = UUID.randomUUID().toString();

		request.setAttribute(LOG_ID, uuid);

		if (handler instanceof HandlerMethod) {
			HandlerMethod hm = (HandlerMethod)handler;
		}

		log.info("REQUEST [{}][{}][{}]", uuid, requestURI, handler);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
		ModelAndView modelAndView) throws Exception {

		// 컨트롤러 실행 후, 뷰 렌더링 전에 실행되는 메서드. ModelAndView 객체를 로그로 출력
		log.info("postHandler [{}]", modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		String requestURI = request.getRequestURI();
		Object logId = request.getAttribute(LOG_ID);
		long startTime = (Long) request.getAttribute(START_TIME);
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;

		log.info("RESPONSE [{}][{}][{}] - Duration: {} ms", logId, requestURI, handler, duration);

		if (ex != null) {
			log.error("afterCompletion error!!", ex);
		}
	}
}
