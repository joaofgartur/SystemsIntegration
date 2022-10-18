package com.example.server.repository;

import com.example.server.entity.Professor;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ProfessorRepository extends ReactiveCrudRepository<Professor, Integer> {
    @Query("SELECT * FROM professor WHERE name = :name")
    Flux<Professor> findByName(String name);
}
