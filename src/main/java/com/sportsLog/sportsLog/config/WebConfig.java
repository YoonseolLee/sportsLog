package com.sportsLog.sportsLog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.sportsLog.sportsLog.interceptor.LogInterceptor;
import com.sportsLog.sportsLog.interceptor.LoggedInInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final LogInterceptor logInterceptor;
	private final LoggedInInterceptor loggedInInterceptor;


	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(logInterceptor)
			.order(1)
			.addPathPatterns("/**");

		registry.addInterceptor(loggedInInterceptor)
			.order(2)
			.addPathPatterns("/user/signup");
	}
}
