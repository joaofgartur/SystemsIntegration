package com.example.server.repository;

import com.example.server.entity.Student;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface StudentRepository extends ReactiveCrudRepository<Student, Integer> {
    @Query("SELECT * FROM student WHERE name = :name")
    Flux<Student> findByName(String name);
}
