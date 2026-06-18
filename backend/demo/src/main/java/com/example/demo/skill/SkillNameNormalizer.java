package com.example.demo.skill;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public final class SkillNameNormalizer {

	private SkillNameNormalizer() {

	}

	public static String normalize(String value) {
		return value.trim().toLowerCase(Locale.ROOT);
	}

	public static List<String> normalizeAll(List<String> rawSkills) {
		if (rawSkills == null) {
			return List.of();
		}

		Set<String> normalized = new LinkedHashSet<>();
		for (String skill : rawSkills) {
			if (skill == null) {
				continue;
			}
			String normalizedSkill = normalize(skill);
			if (!normalizedSkill.isBlank()) {
				normalized.add(normalizedSkill);
			}
		}

		return List.copyOf(normalized);
	}
}
