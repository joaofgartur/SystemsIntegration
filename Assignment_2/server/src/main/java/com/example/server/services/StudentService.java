package com.example.server.services;

import com.example.server.entity.Student;
import com.example.server.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public Mono<Student> addStudent(Student st) {
        return studentRepository.save(st);
    }

    public Flux<Student> getAllStudents() {
        return studentRepository.findAll();
    }
}
