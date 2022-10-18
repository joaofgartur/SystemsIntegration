package com.example.server.services;

import com.example.server.entity.Professor;
import com.example.server.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProfessorService {
    @Autowired
    private ProfessorRepository professorRepository;

    public Mono<Professor> addProfessors(Professor p) {
        return professorRepository.save(p);
    }

    public Flux<Professor> getAllProfessors() {
        return professorRepository.findAll();
    }
}
