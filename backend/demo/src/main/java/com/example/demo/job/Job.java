package com.example.demo.job;

import com.example.demo.skill.Skill;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "jobs")
public class Job {

	private static final int TITLE_MAX_LENGTH = 150;
	private static final int COMPANY_MAX_LENGTH = 120;
	private static final int DESCRIPTION_MAX_LENGTH = 2000;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = TITLE_MAX_LENGTH)
	private String title;

	@Column(nullable = false, length = COMPANY_MAX_LENGTH)
	private String company;

	@Column(nullable = false, length = DESCRIPTION_MAX_LENGTH)
	private String description;

	@ManyToMany
	@JoinTable(
			name = "job_skills",
			joinColumns = @JoinColumn(name = "job_id"),
			inverseJoinColumns = @JoinColumn(name = "skill_id")
	)
	private Set<Skill> requiredSkills = new HashSet<>();

	protected Job() {

	}

	public Job(String title, String company, String description) {
		this.title = title;
		this.company = company;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Skill> getRequiredSkills() {
		return requiredSkills;
	}
}
