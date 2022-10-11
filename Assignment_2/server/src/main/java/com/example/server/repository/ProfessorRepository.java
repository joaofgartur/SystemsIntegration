package com.example.server.repository;

import com.example.server.entity.Professor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ProfessorRepository extends ReactiveCrudRepository<Professor, Integer> {
}
