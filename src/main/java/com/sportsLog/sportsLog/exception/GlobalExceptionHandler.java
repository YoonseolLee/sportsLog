package com.sportsLog.sportsLog.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleBeanValidationException(MethodArgumentNotValidException ex) {
		Map<String, String> errorResult = new HashMap<>();

		ex.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError)error).getField();
			String errorMessage = error.getDefaultMessage();
			errorResult.put(fieldName, errorMessage);
		});
		return errorResult;
	}

	@ExceptionHandler(BoardException.class)
	public ResponseEntity<Map<String, String>> handleBoardException(BoardException ex) {
		Map<String, String> errors = new HashMap<>();
		errors.put("board exception", ex.getMessage());
		return ResponseEntity.badRequest().body(errors);
	}

	@ExceptionHandler(LoginFailedException.class)
	public ResponseEntity<Map<String, String>> handleLoginFailedException(LoginFailedException ex) {
		Map<String, String> errors = new HashMap<>();
		errors.put("loginFail", ex.getMessage());
		return ResponseEntity.badRequest().body(errors);
	}

	@ExceptionHandler(LogoutFailedException.class)
	public ResponseEntity<Map<String, String>> handleLogoutFailedException(LogoutFailedException ex) {
		Map<String, String> errors = new HashMap<>();
		errors.put("logoutFail", ex.getMessage());
		return ResponseEntity.badRequest().body(errors);
	}

	@ExceptionHandler(Exception.class)
	public Map<String, String> exHandler(Exception ex) {
		log.error("Unhandled exception occurred: ", ex);
		Map<String, String> errorResult = new HashMap<>();

		errorResult.put("error", "An unexpected error occurred");
		return errorResult;
	}
}
