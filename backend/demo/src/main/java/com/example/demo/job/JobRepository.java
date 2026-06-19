package com.example.demo.job;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface JobRepository extends JpaRepository<Job, Long> {

	@Query("""
			select distinct j
			from Job j
			join j.requiredSkills s
			where s.name in :skillNames
			""")
	List<Job> findAllMatchingAnySkill(@Param("skillNames") Set<String> skillNames);
}
