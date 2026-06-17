package com.example.demo.skill;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface SkillRepository extends JpaRepository<Skill, Long> {

	Optional<Skill> findByName(String name);

	List<Skill> findAllByNameIn(Set<String> names);
}
