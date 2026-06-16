package com.example.demo.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JwtServiceTest {

	@Test
	void generateAndParseTokenShouldReturnSameSubject() {
		String secret = "test-secret-key-must-be-long-enough-for-hmac-sha";
		long expirationMs = 3_600_000L;
		JwtService jwtService = new JwtService(secret, expirationMs);

		String subject = "candidate@example.com";
		String token = jwtService.generateToken(subject);
		String extractedSubject = jwtService.extractSubject(token);

		assertNotNull(token);
		assertEquals(subject, extractedSubject);
	}
}
