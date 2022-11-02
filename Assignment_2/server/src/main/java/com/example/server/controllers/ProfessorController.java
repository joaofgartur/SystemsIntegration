package com.example.server.controllers;

import com.example.server.entity.Professor;
import com.example.server.services.ProfessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/professor")
public class ProfessorController {

    Logger logger = LoggerFactory.getLogger(ProfessorController.class);

    @Autowired
    private ProfessorService professorService;

    @PostMapping("/add")
    public Mono<Professor> addProfessor(@RequestBody Professor professor) {
        logger.info("[ENDPOINT] addProfessor with contents: {}", professor);
        return professorService.addProfessor(professor);
    }

    @GetMapping("/{professorId}")
    public Mono<ResponseEntity<Professor>> getProfessorById(@PathVariable int professorId) {
        logger.info("[ENDPOINT] getProfessorById with ID {} was called", professorId);
        return professorService.getProfessor(professorId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/")
    public Flux<Professor> getAllProfessors() {
        logger.info("[ENDPOINT] getAllProfessors was called");
        return professorService.getAllProfessors();
    }

    @PutMapping("/{professorId}")
    public Mono<Professor> updateProfessorById(@PathVariable int professorId, @RequestBody Mono<Professor> professorMono) {
        logger.info("[ENDPOINT] updateProfessorById with ID{} and contents: {}", professorId, professorMono);
        return professorService.updateProfessorById(professorId, professorMono);
    }

}
