package com.example.demo.skill;

import com.example.demo.job.Job;
import com.example.demo.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
		name = "skills",
		uniqueConstraints = {
				@UniqueConstraint(name = "unique_skill_name", columnNames = "name")
		}
)
public class Skill {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 80)
	private String name;

	@ManyToMany(mappedBy = "skills")
	private Set<User> users = new HashSet<>();

	@ManyToMany(mappedBy = "requiredSkills")
	private Set<Job> jobs = new HashSet<>();

	protected Skill() {

	}

	public Skill(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<User> getUsers() {
		return users;
	}

	public Set<Job> getJobs() {
		return jobs;
	}
}
