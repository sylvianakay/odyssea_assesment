package com.example.demo.auth;

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

	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public void register(RegisterRequest request) {
		String normalizedEmail = request.getEmail().trim().toLowerCase();
		if (userRepository.findByEmail(normalizedEmail).isPresent()) {
			throw new IllegalArgumentException("Email is already in use.");
		}

		String hashedPassword = passwordEncoder.encode(request.getPassword());
		User user = new User(normalizedEmail, hashedPassword);
		userRepository.save(user);
	}
}
