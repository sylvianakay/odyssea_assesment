package com.example.demo.skill.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class SkillsUpdateRequest {

	@NotNull
	private List<String> skills;

	public List<String> getSkills() {
		return skills;
	}

	public void setSkills(List<String> skills) {
		this.skills = skills;
	}
}
