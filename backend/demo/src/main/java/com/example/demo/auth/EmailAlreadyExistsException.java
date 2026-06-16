package com.example.demo.auth;

public class EmailAlreadyExistsException extends RuntimeException {

	public EmailAlreadyExistsException(String message) {
		super(message);
	}
}
