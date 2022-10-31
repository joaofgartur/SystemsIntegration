package com.example.server.services;

import com.example.server.entity.StudentProfessor;
import com.example.server.repository.StudentProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StudentProfessorService {
    @Autowired
    private StudentProfessorRepository student_professorRepository;

    public Mono<StudentProfessor> addRelationship(StudentProfessor relationship) {
        return student_professorRepository.save(relationship);
    }

    public Mono<Void> deleteRelationship(int relationshipId) {
        return student_professorRepository.deleteById(relationshipId);
    }

    public Flux<StudentProfessor> readAllRelationsFromStudent(int student_id){
        return student_professorRepository.retrieveAllProfessorsFromStudent(student_id);
    }

    public Flux<StudentProfessor> readAllRelationsFromProfessor(int professor_id){
        return student_professorRepository.retrieveAllStudentsFromProfessor(professor_id);
    }
}
