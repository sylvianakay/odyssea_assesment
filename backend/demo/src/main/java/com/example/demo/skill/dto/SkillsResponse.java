package com.example.demo.skill.dto;

import java.util.List;

public class SkillsResponse {

	private final List<String> skills;

	public SkillsResponse(List<String> skills) {
		this.skills = skills;
	}

	public List<String> getSkills() {
		return skills;
	}
}
