package com.example.server.repository;

import com.example.server.entity.StudentProfessor;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface StudentProfessorRepository extends ReactiveCrudRepository<StudentProfessor, Integer> {

    @Query("SELECT * FROM student_professor WHERE student_id = :studentId")
    Flux<StudentProfessor> retrieveAllProfessorsFromStudent(int studentId);

    @Query("SELECT * FROM student_professor WHERE professor_id = :professorId")
    Flux<StudentProfessor> retrieveAllStudentsFromProfessor(int professorId);

}
