package com.example.server.controllers;

import com.example.server.entity.Professor;
import com.example.server.entity.Student;
import com.example.server.services.ProfessorService;
import com.example.server.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/create")
    public Mono<Student> createStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @GetMapping("/")
    public Flux<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

}
