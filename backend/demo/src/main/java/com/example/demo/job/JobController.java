package com.example.demo.job;

import com.example.demo.job.dto.JobMatchResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

	private final JobMatchService jobMatchService;

	public JobController(JobMatchService jobMatchService) {
		this.jobMatchService = jobMatchService;
	}

	@GetMapping("/matches")
	public List<JobMatchResponse> getJobMatches(Authentication authentication) {
		return jobMatchService.getMatchesForUser(authentication.getName());
	}
}
