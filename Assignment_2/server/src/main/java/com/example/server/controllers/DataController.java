package com.example.server.controllers;

import com.example.server.entity.Professor;
import com.example.server.entity.Student;
import com.example.server.entity.StudentProfessor;
import com.example.server.services.ProfessorService;
import com.example.server.services.StudentProfessorService;
import com.example.server.services.StudentService;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/generate-data")
public class DataController {
    @Autowired
    private  StudentService studentService;
    @Autowired
    private ProfessorService professorService;
    @Autowired
    private StudentProfessorService studentProfessorService;

    @PostMapping("/")
    public void generateData(){
        System.out.println("HI");
        int ENTITIES = 15;

        Faker faker = new Faker();

        String fromDateStr = "01/01/1990";
        String toDateStr = "01/01/2005";
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date fromDate = formatter.parse(fromDateStr);
            Date toDate = formatter.parse(toDateStr);
            List<Student> studentList = new ArrayList<>();
            for (int i = 0; i < ENTITIES; i++) {
                String name = faker.name().firstName();
                String date = formatter.format(faker.date().between(fromDate, toDate));
                int credits = i < 2 ? 180 : faker.number().numberBetween(0, 180);
                int grade = faker.number().numberBetween(7, 19);
                studentService.addStudent(new Student(name, date, credits, grade)).subscribe();
            }

            List<Professor> professorList = new ArrayList<>();
            for (int i = 0; i < ENTITIES; i++) {
                String name = faker.name().firstName();
                professorService.addProfessor(new Professor(name)).subscribe();
            }

            for (int i = 0; i < ENTITIES; i++) {
                ArrayList<Integer> profs = new ArrayList<>();
                int numProfessors = faker.number().numberBetween(0,5);
                for (int j = 0; j < numProfessors; j++) {
                    int randomProf = faker.number().numberBetween(0, ENTITIES);
                    if (!profs.contains(randomProf)) {
                        studentProfessorService.addRelationship(new StudentProfessor(i+1, randomProf)).subscribe();
                        profs.add(randomProf);
                    }
                }
            }

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}