package com.example.demo.auth;

import com.example.demo.auth.dto.LoginRequest;
import com.example.demo.auth.dto.RegisterRequest;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}

	@Transactional
	public void register(RegisterRequest request) {
		String normalizedEmail = request.getEmail().trim().toLowerCase();
		if (userRepository.findByEmail(normalizedEmail).isPresent()) {
			throw new EmailAlreadyExistsException("Email is already in use.");
		}

		String hashedPassword = passwordEncoder.encode(request.getPassword());
		User user = new User(normalizedEmail, hashedPassword);
		userRepository.save(user);
	}

	@Transactional(readOnly = true)
	public String login(LoginRequest request) {
		String normalizedEmail = request.getEmail().trim().toLowerCase();
		User user = userRepository.findByEmail(normalizedEmail)
				.orElseThrow(() -> new InvalidCredentialsException("Invalid email or password."));

		boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());
		if (!passwordMatches) {
			throw new InvalidCredentialsException("Invalid email or password.");
		}

		return jwtService.generateToken(user.getEmail());
	}
}
