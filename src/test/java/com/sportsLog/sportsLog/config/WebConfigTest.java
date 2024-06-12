package com.sportsLog.sportsLog.config;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import com.sportsLog.sportsLog.interceptor.LogInterceptor;
import com.sportsLog.sportsLog.interceptor.LoggedInInterceptor;

@SpringBootTest
public class WebConfigTest {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	public void testLoggedInInterceptorInjection() {
		LoggedInInterceptor loggedInInterceptor = applicationContext.getBean(LoggedInInterceptor.class);
		assertThat(loggedInInterceptor).isNotNull();
	}

	@Test
	public void testLogInterceptorInjection() {
		LogInterceptor logInterceptor = applicationContext.getBean(LogInterceptor.class);
		assertThat(logInterceptor).isNotNull();
	}
}