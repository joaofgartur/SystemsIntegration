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

    public Mono<Student> getStudentById(int studentId) {
        return studentRepository.findById(studentId);
    }

    public Mono<Student> updateStudentById(int studentId, Mono<Student> studentMono) {
        return studentRepository.findById(studentId)
                .flatMap(student -> studentMono.map(s -> {
                    student.setName(s.getName());
                    student.setBirthdate(s.getBirthdate());
                    student.setCredits(s.getCredits());
                    student.setAverageGrade(s.getAverageGrade());
                    return student;
                }))
                .flatMap(student -> studentRepository.save(student));
    }

    public Mono<Void> deleteStudentById(int studentId){
        /*studentRepository.deleteStudentFromRelations(studentId);*/
        return studentRepository.deleteById(studentId);
    }
}
