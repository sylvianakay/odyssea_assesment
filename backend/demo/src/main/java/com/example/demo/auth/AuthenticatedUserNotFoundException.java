package com.example.demo.auth;

public class AuthenticatedUserNotFoundException extends RuntimeException {

	public AuthenticatedUserNotFoundException(String message) {
		super(message);
	}
}
