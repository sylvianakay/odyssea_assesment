package com.example.demo.auth.dto;

import com.example.demo.auth.AuthValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

	@Email
	@NotBlank
	@Size(max = AuthValidationConstants.EMAIL_MAX_LENGTH)
	private String email;

	@NotBlank
	@Size(
			min = AuthValidationConstants.PASSWORD_MIN_LENGTH,
			max = AuthValidationConstants.PASSWORD_MAX_LENGTH
	)
	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
