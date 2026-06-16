package com.example.demo.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SecurityBeansConfigTest {

	private final SecurityBeansConfig securityBeansConfig = new SecurityBeansConfig();

	@Test
	void passwordEncoderHashesAndMatches() {
		PasswordEncoder passwordEncoder = securityBeansConfig.passwordEncoder();
		String rawPassword = "MySecretPass123!";

		String hashedPassword = passwordEncoder.encode(rawPassword);

		assertNotEquals(rawPassword, hashedPassword);
		assertTrue(passwordEncoder.matches(rawPassword, hashedPassword));
		assertFalse(passwordEncoder.matches("wrong-password", hashedPassword));
	}
}
