package com.example.demo.common;

import com.example.demo.auth.EmailAlreadyExistsException;
import com.example.demo.auth.AuthenticatedUserNotFoundException;
import com.example.demo.auth.InvalidCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(EmailAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public Map<String, String> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
		Map<String, String> error = new LinkedHashMap<>();
		error.put("message", ex.getMessage());
		return error;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, Object> handleValidationException(MethodArgumentNotValidException ex) {
		Map<String, Object> error = new LinkedHashMap<>();
		error.put("message", "Invalid request body.");
		Map<String, String> fieldErrors = new LinkedHashMap<>();
		for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
			fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
		}
		error.put("errors", fieldErrors);
		return error;
	}

	@ExceptionHandler(InvalidCredentialsException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public Map<String, String> handleInvalidCredentialsException(InvalidCredentialsException ex) {
		Map<String, String> error = new LinkedHashMap<>();
		error.put("message", ex.getMessage());
		return error;
	}

	@ExceptionHandler(AuthenticatedUserNotFoundException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public Map<String, String> handleAuthenticatedUserNotFoundException(AuthenticatedUserNotFoundException ex) {
		Map<String, String> error = new LinkedHashMap<>();
		error.put("message", ex.getMessage());
		return error;
	}
}
