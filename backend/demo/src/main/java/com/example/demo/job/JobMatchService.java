package com.example.demo.job;

import com.example.demo.auth.AuthenticatedUserNotFoundException;
import com.example.demo.job.dto.JobMatchResponse;
import com.example.demo.skill.SkillNameNormalizer;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JobMatchService {

	private final UserRepository userRepository;
	private final JobRepository jobRepository;

	public JobMatchService(UserRepository userRepository, JobRepository jobRepository) {
		this.userRepository = userRepository;
		this.jobRepository = jobRepository;
	}

	@Transactional(readOnly = true)
	public List<JobMatchResponse> getMatchesForUser(String email) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new AuthenticatedUserNotFoundException("Authenticated user not found."));

		Set<String> userSkills = user.getSkills().stream()
				.map(skill -> SkillNameNormalizer.normalize(skill.getName()))
				.collect(Collectors.toSet());

		if (userSkills.isEmpty()) {
			return List.of();
		}

		return jobRepository.findAll().stream()
				.map(job -> {
					List<String> matchedSkills = job.getRequiredSkills().stream()
							.map(skill -> SkillNameNormalizer.normalize(skill.getName()))
							.filter(userSkills::contains)
							.distinct()
							.sorted()
							.toList();

					if (matchedSkills.isEmpty()) {
						return null;
					}

					return new JobMatchResponse(
							job.getId(),
							job.getTitle(),
							job.getCompany(),
							job.getDescription(),
							matchedSkills
					);
				})
				.filter(response -> response != null)
				.toList();
	}
}
