package com.example.server.controllers;

import com.example.server.entity.Student;
import com.example.server.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/add")
    public Mono<Student> addStudent(@RequestBody Student studentMono) {
        return studentService.addStudent(studentMono);
    }

    @GetMapping("/{studentId}")
    public Mono<ResponseEntity<Student>> getStudentById(@PathVariable int studentId) {
        return studentService.getStudentById(studentId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/")
    public Flux<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @PutMapping("/{studentId}")
    public Mono<Student> updateStudentById(@PathVariable int studentId, @RequestBody Mono<Student> studentMono) {
        return studentService.updateStudentById(studentId, studentMono);
    }

}
