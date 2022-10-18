package com.example.server.controllers;

import com.example.server.entity.Professor;
import com.example.server.services.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/professor")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    @PostMapping("/register")
    public Mono<Professor> createProfessor(@RequestBody Professor professor) {
        return professorService.addProfessors(professor);
    }

    @GetMapping("/")
    public Flux<Professor> getAllProfessors() {
        return professorService.getAllProfessors();
    }

}
