package org.example;

import entity.Student;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import java.lang.Thread;
import java.io.*;
import java.util.Comparator;

public class ReactiveClient {
    private final String BASE_URL = "http://localhost:8080";

    public ReactiveClient() {
        myMain();
    }

    private void myMain() {
        WebClient client = WebClient.create(BASE_URL);

        try {
            // File file = new File("newfile.json");
            // FileOutputStream fop = new FileOutputStream(file);

            Flux<Student> allStudentsFlux = client.get()
                    .uri("/student/")
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToFlux(Student.class);

            PrintWriter writer = new PrintWriter("1.txt");

            // exercise 1
            // allStudentsFlux.subscribe(s -> writer.println("{Name: " + s.getName() + ", Birthdate: " + s.getBirthdate() + "}"));

            // exercise 2
            /*
            allStudentsFlux
                    .count()
                    .subscribe(count -> writer.println("Count: " + count));

             */

            //exercise 3
            /*
            allStudentsFlux
                    .filter(student -> student.getCredits() < 180)
                    .count()
                    .subscribe(count -> writer.println("Active students: " + count));
             */

            //exercise 4
            /*
            allStudentsFlux
                    .map(Student::getCredits)
                    .reduce(Integer::sum)
                    .subscribe(sum -> writer.println(sum / 6));*/

            // exercise 5
            allStudentsFlux
                    .filter(student -> student.getCredits() >= 120 && student.getCredits() < 180)
                    .sort(Comparator.comparing(Student::getCredits))
                    .subscribe(writer::println);


            Thread.sleep(2000);

            writer.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }



    public static void main(String[] args) {
        ReactiveClient client = new ReactiveClient();
    }
}