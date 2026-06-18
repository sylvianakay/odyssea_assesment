package com.example.demo.auth;

import com.example.demo.common.ApiExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest {

	private MockMvc mockMvc;
	private AuthService authService;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp() {
		authService = Mockito.mock(AuthService.class);
		AuthController authController = new AuthController(authService);

		LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
		validator.afterPropertiesSet();

		mockMvc = MockMvcBuilders.standaloneSetup(authController)
				.setControllerAdvice(new ApiExceptionHandler())
				.setValidator(validator)
				.build();
	}

	@Test
	void registerExistingEmailShouldReturn409() throws Exception {
		Mockito.doThrow(new EmailAlreadyExistsException("Email is already in use."))
				.when(authService).register(Mockito.any());

		mockMvc.perform(post("/api/auth/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(
								Map.of("email", "candidate@example.com", "password", "Password123!")
						)))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.message").value("Email is already in use."));
	}

	@Test
	void loginWrongPasswordShouldReturn401() throws Exception {
		Mockito.when(authService.login(Mockito.any()))
				.thenThrow(new InvalidCredentialsException("Invalid email or password."));

		mockMvc.perform(post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(
								Map.of("email", "candidate@example.com", "password", "wrong-password")
						)))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Invalid email or password."));
	}

	@Test
	void invalidRequestBodyShouldReturn400() throws Exception {
		mockMvc.perform(post("/api/auth/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(
								Map.of("email", "invalid-email", "password", "")
						)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Invalid request body."))
				.andExpect(jsonPath("$.errors.email").exists())
				.andExpect(jsonPath("$.errors.password").exists());
	}
}
