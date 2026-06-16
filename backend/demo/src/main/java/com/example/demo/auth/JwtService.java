package com.example.demo.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

	private final SecretKey signingKey;
	private final long expirationMs;

	public JwtService(
			@Value("${jwt.secret}") String secret,
			@Value("${jwt.expiration-ms}") long expirationMs
	) {
		this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		this.expirationMs = expirationMs;
	}

	public String generateToken(String subject) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + expirationMs);

		return Jwts.builder()
				.subject(subject)
				.issuedAt(now)
				.expiration(expiryDate)
				.signWith(signingKey)
				.compact();
	}

	public String extractSubject(String token) {
		return extractAllClaims(token).getSubject();
	}

	public boolean isTokenValid(String token) {
		try {
			extractAllClaims(token);
			return true;
		} catch (JwtException | IllegalArgumentException ex) {
			return false;
		}
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser()
				.verifyWith(signingKey)
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
}
