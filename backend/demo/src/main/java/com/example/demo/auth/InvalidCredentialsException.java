package com.example.demo.auth;

public class InvalidCredentialsException extends RuntimeException {

	public InvalidCredentialsException(String message) {
		super(message);
	}
}
