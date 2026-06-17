package com.example.demo.skill;

import com.example.demo.skill.dto.SkillsResponse;
import com.example.demo.skill.dto.SkillsUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users/me")
public class UserSkillController {

	private final UserSkillService userSkillService;

	public UserSkillController(UserSkillService userSkillService) {
		this.userSkillService = userSkillService;
	}

	@GetMapping("/skills")
	public SkillsResponse getMySkills(Authentication authentication) {
		// jwt subject is stored as authentication name by the security filter
		List<String> skills = userSkillService.getSkillsForUser(authentication.getName());
		return new SkillsResponse(skills);
	}

	@PutMapping("/skills")
	public SkillsResponse replaceMySkills(
			Authentication authentication,
			@Valid @RequestBody SkillsUpdateRequest request
	) {
		// replacing the entire skill list for the authenticated user
		List<String> skills = userSkillService.replaceSkillsForUser(authentication.getName(), request.getSkills());
		return new SkillsResponse(skills);
	}
}
