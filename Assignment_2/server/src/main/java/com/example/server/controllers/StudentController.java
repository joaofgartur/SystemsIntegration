package com.example.server.controllers;

import com.example.server.entity.Student;
import com.example.server.services.StudentProfessorService;
import com.example.server.services.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/student")
public class StudentController {

    Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentProfessorService studentProfessorService;

    @PostMapping("/add")
    public Mono<Student> addStudent(@RequestBody Student studentMono) {
        logger.info("[ENDPOINT] addStudent with contents: {}", studentMono);
        return studentService.addStudent(studentMono);
    }

    @GetMapping("/{studentId}")
    public Mono<ResponseEntity<Student>> getStudentById(@PathVariable int studentId) {
        logger.info("[ENDPOINT] getStudentById with ID {}", studentId);
        return studentService.getStudentById(studentId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/")
    public Flux<Student> getAllStudents() {
        logger.info("[ENDPOINT] getAllStudents was called");
        return studentService.getAllStudents();
    }

    @PutMapping("/{studentId}")
    public Mono<Student> updateStudentById(@PathVariable int studentId, @RequestBody Mono<Student> studentMono) {
        logger.info("[ENDPOINT] updateStudentById with ID {} and contents: {}", studentId, studentMono);
        return studentService.updateStudentById(studentId, studentMono);
    }

    @DeleteMapping("/{studentId}")
    public Mono<Void> deleteStudentById(@PathVariable int studentId) {
        //studentProfessorService.readAllRelationsFromStudent(studentId).count();
        /* Se tiver relações dá erro, se não tiver faz a linha a baixo: */
        /*return studentService.deleteStudentById(studentId);*/
        return null;
    }

}
