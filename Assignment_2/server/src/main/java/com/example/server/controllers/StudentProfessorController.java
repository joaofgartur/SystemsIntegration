package com.example.server.controllers;

import com.example.server.entity.StudentProfessor;
import com.example.server.services.StudentProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/relationship")
public class StudentProfessorController {
    @Autowired
    private StudentProfessorService student_professorService;

    @PostMapping("/add")
    public Mono<StudentProfessor> addRelationship(@RequestBody StudentProfessor student_professor) {
        return student_professorService.addRelationship(student_professor);
    }

    @DeleteMapping("/{relationshipId}")
    public Mono<Void> deleteRelationship(@PathVariable int relationshipId) {
        return student_professorService.deleteRelationship(relationshipId);
    }

    @GetMapping("/student/{studentId}")
    public Flux<StudentProfessor> getAllProfessorsFromStudent(@PathVariable int studentId){
        return student_professorService.readAllRelationsFromStudent(studentId);
    }

   @GetMapping("/professor/{professorId}")
    public Flux<StudentProfessor> getAllStudentsFromProfessor(@PathVariable int professorId){
        return student_professorService.readAllRelationsFromProfessor(professorId);
    }

}
