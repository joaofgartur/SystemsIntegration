package com.example.server.controllers;

import com.example.server.entity.StudentProfessor;
import com.example.server.services.StudentProfessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/relationship")
public class StudentProfessorController {

    Logger logger = LoggerFactory.getLogger(StudentProfessorController.class);
    @Autowired
    private StudentProfessorService student_professorService;

    @PostMapping("/add")
    public Mono<StudentProfessor> addRelationship(@RequestBody StudentProfessor student_professor) {
        logger.info("[ENDPOINT] addRelationship with content: {}", student_professor);
        return student_professorService.addRelationship(student_professor);
    }

    @DeleteMapping("/{relationshipId}")
    public Mono<Void> deleteRelationship(@PathVariable int relationshipId) {
        logger.info("[ENDPOINT] deleteRelationship with ID {}", relationshipId);
        return student_professorService.deleteRelationship(relationshipId);
    }

    @GetMapping("/student/{studentId}")
    public Flux<StudentProfessor> getAllProfessorsFromStudent(@PathVariable int studentId){
        logger.info("[ENDPOINT] getAllProfessorsFromStudent with StudentID {}", studentId);
        return student_professorService.readAllRelationsFromStudent(studentId);
    }

   @GetMapping("/professor/{professorId}")
    public Flux<StudentProfessor> getAllStudentsFromProfessor(@PathVariable int professorId){
       logger.info("[ENDPOINT] getAllStudentsFromProfessor with ProfessorID {}", professorId);
        return student_professorService.readAllRelationsFromProfessor(professorId);
    }

}
