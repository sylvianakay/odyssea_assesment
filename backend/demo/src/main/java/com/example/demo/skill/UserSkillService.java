package com.example.demo.skill;

import com.example.demo.auth.InvalidCredentialsException;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class UserSkillService {

	private final UserRepository userRepository;
	private final SkillRepository skillRepository;

	public UserSkillService(UserRepository userRepository, SkillRepository skillRepository) {
		this.userRepository = userRepository;
		this.skillRepository = skillRepository;
	}

	@Transactional(readOnly = true)
	public List<String> getSkillsForUser(String email) {
		User user = findUserByEmail(email);
		return user.getSkills().stream()
				.map(Skill::getName)
				.sorted()
				.toList();
	}

	@Transactional
	public List<String> replaceSkillsForUser(String email, List<String> rawSkills) {
		User user = findUserByEmail(email);
		List<String> normalizedSkills = normalizeSkills(rawSkills);
		Set<Skill> skills = resolveSkills(normalizedSkills);

		user.getSkills().clear();
		user.getSkills().addAll(skills);

		return user.getSkills().stream()
				.map(Skill::getName)
				.sorted()
				.toList();
	}

	private User findUserByEmail(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new InvalidCredentialsException("Authenticated user not found."));
	}

	private List<String> normalizeSkills(List<String> rawSkills) {
		// normalizing skills once so storage and matching are case-insensitive and consistent.
		return SkillNameNormalizer.normalizeAll(rawSkills);
	}

	private Set<Skill> resolveSkills(List<String> normalizedSkills) {
		if (normalizedSkills.isEmpty()) {
			return new LinkedHashSet<>();
		}

		Set<String> normalizedSkillSet = new LinkedHashSet<>(normalizedSkills);
		List<Skill> existingSkills = skillRepository.findAllByNameIn(normalizedSkillSet);
		Map<String, Skill> existingByName = new LinkedHashMap<>();
		for (Skill existingSkill : existingSkills) {
			existingByName.put(existingSkill.getName(), existingSkill);
		}

		List<Skill> resolvedSkills = new ArrayList<>();
		for (String skillName : normalizedSkills) {
			Skill resolved = existingByName.get(skillName);
			if (resolved == null) {
				// creating only missing skills, existing ones are reused.
				resolved = skillRepository.save(new Skill(skillName));
				existingByName.put(skillName, resolved);
			}
			resolvedSkills.add(resolved);
		}

		resolvedSkills.sort(Comparator.comparing(Skill::getName));
		return new LinkedHashSet<>(resolvedSkills);
	}
}
