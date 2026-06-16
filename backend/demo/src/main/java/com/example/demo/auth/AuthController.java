package com.example.demo.auth;

import com.example.demo.auth.dto.LoginRequest;
import com.example.demo.auth.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public String register(@Valid @RequestBody RegisterRequest request) {
		authService.register(request);
		return "User registered successfully.";
	}

	@PostMapping("/login")
	@ResponseStatus(HttpStatus.OK)
	public String login(@Valid @RequestBody LoginRequest request) {
		authService.login(request);
		return "login ok";
	}
}
