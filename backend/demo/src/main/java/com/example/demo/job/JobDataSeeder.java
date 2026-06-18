package com.example.demo.job;

import com.example.demo.skill.Skill;
import com.example.demo.skill.SkillNameNormalizer;
import com.example.demo.skill.SkillRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class JobDataSeeder implements ApplicationRunner {

	private static final String JOBS_JSON_PATH = "jobs.json";
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private final JobRepository jobRepository;
	private final SkillRepository skillRepository;

	public JobDataSeeder(JobRepository jobRepository, SkillRepository skillRepository) {
		this.jobRepository = jobRepository;
		this.skillRepository = skillRepository;
	}

	@Override
	@Transactional
	public void run(ApplicationArguments args) {
		if (jobRepository.count() > 0) {
			return;
		}

		List<SeedJob> seedJobs = readSeedJobs();
		if (seedJobs.isEmpty()) {
			return;
		}

		Set<String> allNormalizedSkills = new LinkedHashSet<>();
		for (SeedJob seedJob : seedJobs) {
			allNormalizedSkills.addAll(SkillNameNormalizer.normalizeAll(seedJob.requiredSkills()));
		}

		Map<String, Skill> skillsByName = new HashMap<>();
		if (!allNormalizedSkills.isEmpty()) {
			for (Skill existing : skillRepository.findAllByNameIn(allNormalizedSkills)) {
				skillsByName.put(existing.getName(), existing);
			}
		}

		for (String skillName : allNormalizedSkills) {
			if (!skillsByName.containsKey(skillName)) {
				Skill created = skillRepository.save(new Skill(skillName));
				skillsByName.put(skillName, created);
			}
		}

		for (SeedJob seedJob : seedJobs) {
			Job job = new Job(seedJob.title(), seedJob.company(), seedJob.description());
			List<String> normalizedRequiredSkills = SkillNameNormalizer.normalizeAll(seedJob.requiredSkills());

			for (String skillName : normalizedRequiredSkills) {
				Skill skill = skillsByName.get(skillName);
				if (skill != null) {
					job.getRequiredSkills().add(skill);
				}
			}

			jobRepository.save(job);
		}
	}

	private List<SeedJob> readSeedJobs() {
		ClassPathResource resource = new ClassPathResource(JOBS_JSON_PATH);
		try (InputStream inputStream = resource.getInputStream()) {
			List<SeedJob> parsed = OBJECT_MAPPER.readValue(inputStream, new TypeReference<>() {
			});
			return parsed == null ? List.of() : parsed;
		} catch (IOException ex) {
			throw new IllegalStateException("Failed to load jobs seed data from " + JOBS_JSON_PATH, ex);
		}
	}

	private record SeedJob(
			String title,
			String company,
			String description,
			List<String> requiredSkills
	) {
		private SeedJob {
			if (requiredSkills == null) {
				requiredSkills = new ArrayList<>();
			}
		}
	}
}
