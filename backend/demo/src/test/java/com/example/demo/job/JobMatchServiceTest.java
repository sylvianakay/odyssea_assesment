package com.example.demo.job;

import com.example.demo.job.dto.JobMatchResponse;
import com.example.demo.skill.Skill;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobMatchServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private JobRepository jobRepository;

	@InjectMocks
	private JobMatchService jobMatchService;

	@Test
	void noOverlapShouldReturnNoJobs() {
		User user = createUser("candidate@example.com", "java");
		Job nonMatchingJob = createJob("Design Role", "figma");

		when(userRepository.findByEmail("candidate@example.com")).thenReturn(Optional.of(user));
		when(jobRepository.findAll()).thenReturn(List.of(nonMatchingJob));

		List<JobMatchResponse> matches = jobMatchService.getMatchesForUser("candidate@example.com");

		assertTrue(matches.isEmpty());
	}

	@Test
	void oneOverlapShouldIncludeJob() {
		User user = createUser("candidate@example.com", "java", "sql");
		Job matchingJob = createJob("Backend Role", "java", "docker");
		Job nonMatchingJob = createJob("Design Role", "figma");

		when(userRepository.findByEmail("candidate@example.com")).thenReturn(Optional.of(user));
		when(jobRepository.findAll()).thenReturn(List.of(matchingJob, nonMatchingJob));

		List<JobMatchResponse> matches = jobMatchService.getMatchesForUser("candidate@example.com");

		assertEquals(1, matches.size());
		assertEquals("Backend Role", matches.get(0).getTitle());
		assertEquals(List.of("java"), matches.get(0).getMatchedSkills());
	}

	@Test
	void multipleOverlapsShouldReturnNormalizedDeduplicatedMatchedSkills() {
		User user = createUser("candidate@example.com", "java", "sql");
		Job matchingJob = createJob("Full Stack Role", " Java ", "JAVA", "sql", "docker");

		when(userRepository.findByEmail("candidate@example.com")).thenReturn(Optional.of(user));
		when(jobRepository.findAll()).thenReturn(List.of(matchingJob));

		List<JobMatchResponse> matches = jobMatchService.getMatchesForUser("candidate@example.com");

		assertEquals(1, matches.size());
		assertEquals(List.of("java", "sql"), matches.get(0).getMatchedSkills());
	}

	private User createUser(String email, String... skills) {
		User user = new User(email, "hashed-password");
		for (String skillName : skills) {
			user.getSkills().add(new Skill(skillName));
		}
		return user;
	}

	private Job createJob(String title, String... requiredSkills) {
		Job job = new Job(title, "Acme", "Description");
		for (String skillName : requiredSkills) {
			job.getRequiredSkills().add(new Skill(skillName));
		}
		return job;
	}
}
