package org.example;

import entity.Student;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.io.*;
import java.util.Comparator;
import java.util.List;

public class ReactiveClient {
    private final String BASE_URL = "http://localhost:8080";

    private void solveStudents(Flux<Student> studentsFlux, int exercise) {
        try {
            String filename = String.valueOf(exercise) + ".txt";
            PrintWriter writer = new PrintWriter(filename);

            switch (exercise) {
                case 1:
                    studentsFlux.subscribe(s -> writer.println("{Name: " + s.getName() + ", Birthdate: " + s.getBirthdate() + "}"));
                    break;
                case 2:
                    studentsFlux
                            .count()
                            .subscribe(count -> writer.println("Count: " + count));
                    break;
                case 3:
                    studentsFlux
                            .filter(student -> student.getCredits() < 180)
                            .count()
                            .subscribe(count -> writer.println("Active students: " + count));
                    break;
                case 4:
                    studentsFlux
                            .map(Student::getCredits)
                            .reduce(Integer::sum)
                            .subscribe(sum -> writer.println(sum / 6));
                    break;
                case 5:
                    studentsFlux
                            .filter(student -> student.getCredits() >= 120 && student.getCredits() < 180)
                            .sort(Comparator.comparing(Student::getCredits))
                            .subscribe(writer::println);
                    break;
                case 6:
                    studentsFlux
                            .map(Student::getAverageGrade)
                            .buffer()
                            .subscribe(grades -> writer.println("{Average: " + average(grades) + ", Std: " + standardDeviation(grades) + "}"));
                    break;
                case 7:
                    studentsFlux
                            .filter(student -> student.getCredits() == 180)
                            .map(Student::getAverageGrade)
                            .buffer()
                            .subscribe(grades -> writer.println("{Average: " + average(grades) + ", Std: " + standardDeviation(grades) + "}"));
                    break;
                case 8:
                    break;
                default:
                    writer.print("unsolved");

            }

            Thread.sleep(2000);

            writer.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    private float average(List<Integer> grades) {
        float average = grades.stream().mapToInt(Integer::intValue).sum();
        if (grades.size() > 0) {
            average /= grades.size();
        }

        return average;
    }

    private float standardDeviation(List<Integer> grades) {
        int[] gradesArray = grades.stream().mapToInt(i->i).toArray();
        float mean = average(grades);

        float variance = 0;
        for(int grade: gradesArray) {
            variance += Math.pow(grade - mean, 2);
        }

        return (float) Math.sqrt(variance / gradesArray.length);
    }

    public ReactiveClient() {
        myMain();
    }

    private void myMain() {
        WebClient client = WebClient.create(BASE_URL);

        try {
            // File file = new File("newfile.json");
            // FileOutputStream fop = new FileOutputStream(file);

            Flux<Student> studentsFlux = client.get()
                    .uri("/student/")
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToFlux(Student.class);

            // exercise 1
            this.solveStudents(studentsFlux,1);

            // exercise 2
            this.solveStudents(studentsFlux,2);

            // exercise 3
            this.solveStudents(studentsFlux,3);

            // exercise 4
            this.solveStudents(studentsFlux,4);

            // exercise 5
            this.solveStudents(studentsFlux,5);

            // exercise 6
            this.solveStudents(studentsFlux,6);

            // exercise 7
            this.solveStudents(studentsFlux,7);

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }



    public static void main(String[] args) {
        ReactiveClient client = new ReactiveClient();
    }
}