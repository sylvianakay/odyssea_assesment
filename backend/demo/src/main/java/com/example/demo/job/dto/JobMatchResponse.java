package com.example.demo.job.dto;

import java.util.List;

public class JobMatchResponse {

	private final Long id;
	private final String title;
	private final String company;
	private final String description;
	private final List<String> matchedSkills;

	public JobMatchResponse(Long id, String title, String company, String description, List<String> matchedSkills) {
		this.id = id;
		this.title = title;
		this.company = company;
		this.description = description;
		this.matchedSkills = matchedSkills;
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getCompany() {
		return company;
	}

	public String getDescription() {
		return description;
	}

	public List<String> getMatchedSkills() {
		return matchedSkills;
	}
}
