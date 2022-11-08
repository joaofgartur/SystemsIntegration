package org.example;

import entity.*;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class ReactiveClientMultipleCalls {
    private final String BASE_URL = "http://localhost:8080";
    private void exercise1(WebClient client, String filename, int sleepTime) {
        try {
            Flux<Student> studentsFlux = client
                .get()
                .uri("/student/")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Student.class);

            PrintWriter writer = new PrintWriter(filename);
            studentsFlux.subscribe(s -> writer.println("{Name: " + s.getName() + ", Birthdate: " + s.getBirthdate() + "}"));
            Thread.sleep(sleepTime);
            writer.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private void exercise2(WebClient client, String filename, int sleepTime) {
        try {
            Flux<Student> studentsFlux = client
                .get()
                .uri("/student/")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Student.class);

            PrintWriter writer = new PrintWriter(filename);
            studentsFlux
                    .count()
                    .subscribe(count -> writer.println("{Count: " + count + "}"));
            Thread.sleep(sleepTime);
            writer.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private void exercise3(WebClient client, String filename, int sleepTime) {
        try {
            Flux<Student> studentsFlux = client
                .get()
                .uri("/student/")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Student.class);

            PrintWriter writer = new PrintWriter(filename);
            studentsFlux
                    .filter(student -> student.getCredits() < 180)
                    .count()
                    .subscribe(count -> writer.println("{Number of active students: " + count + "}"));
            Thread.sleep(sleepTime);
            writer.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private void exercise4(WebClient client, String filename, int sleepTime) {
        try {
            Flux<Student> studentsFlux = client
                .get()
                .uri("/student/")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Student.class);

            PrintWriter writer = new PrintWriter(filename);
            studentsFlux
                    .map(Student::getCredits)
                    .reduce(Integer::sum)
                    .subscribe(sum -> writer.println("{Number of completed courses: " + (sum / 6) + "}"));
            Thread.sleep(sleepTime);
            writer.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private void exercise5(WebClient client, String filename, int sleepTime) {
        try {
            Flux<Student> studentsFlux = client
                .get()
                .uri("/student/")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Student.class);

            PrintWriter writer = new PrintWriter(filename);
            studentsFlux
                    .filter(student -> student.getCredits() >= 120 && student.getCredits() < 180)
                    .sort(Comparator.comparing(Student::getCredits))
                    .subscribe(student -> {writer.println("{" + student.getName() + ", Credits: " + student.getCredits() + "}");});
            Thread.sleep(sleepTime);
            writer.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private void exercise6(WebClient client, String filename, int sleepTime) {
        try {
            Flux<Student> studentsFlux = client
                .get()
                .uri("/student/")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Student.class);

            PrintWriter writer = new PrintWriter(filename);
            studentsFlux
                    .map(Student::getAverageGrade)
                    .buffer()
                    .subscribe(grades -> writer.println("{Average: " + average(grades) + ", Std: " + standardDeviation(grades) + "}"));
            Thread.sleep(sleepTime);
            writer.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private void exercise7(WebClient client, String filename, int sleepTime) {
        try {
            Flux<Student> studentsFlux = client
                .get()
                .uri("/student/")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Student.class);

            PrintWriter writer = new PrintWriter(filename);
            studentsFlux
                    .filter(student -> student.getCredits() == 180)
                    .map(Student::getAverageGrade)
                    .buffer()
                    .subscribe(grades -> writer.println("{Average: " + average(grades) + ", Std: " + standardDeviation(grades) + "}"));
            Thread.sleep(sleepTime);
            writer.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private void exercise8(WebClient client, String filename, int sleepTime) {
        try {
            Flux<Student> studentsFlux = client
                    .get()
                    .uri("/student/")
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToFlux(Student.class);

            PrintWriter writer = new PrintWriter(filename);
            studentsFlux.reduce((a, b) -> {
                SimpleDateFormat sm = new SimpleDateFormat("dd/MM/yyyy");

                try {
                    Date dateA = sm.parse(a.getBirthdate());
                    Date dateB = sm.parse(b.getBirthdate());

                    DateComparator comparator = new DateComparator();

                    if (comparator.compare(dateA, dateB) < 0) {
                        return a;
                    } else {
                        return b;
                    }
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

            }).subscribe(student -> {writer.println("{Name: " + student.getName() + "}");});
            Thread.sleep(sleepTime);
            writer.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private void exercise9(WebClient client, String filename, int sleepTime) {
        try {
            Flux<Student> studentsFlux = client
                    .get()
                    .uri("/student/")
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToFlux(Student.class);

            PrintWriter writer = new PrintWriter(filename);

            studentsFlux.flatMap(student -> {
                Flux<StudentProfessor> studentProfessors = client
                        .get()
                        .uri("/relationship/student/" + student.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToFlux(StudentProfessor.class);

                return studentProfessors.count();
            }).buffer().subscribe(array -> writer.println("{Average number of professors per student: " + longAverage(array) + "}"));

            Thread.sleep(sleepTime);
            writer.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    private void exercise10(WebClient client, String filename, int sleepTime) {
        try {
            Flux<Professor> professorsFlux = client
                    .get()
                    .uri("/professor/")
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToFlux(Professor.class);

            PrintWriter writer = new PrintWriter(filename);
            List<ProfessorHelper> helper = new ArrayList<>();

            professorsFlux
                    .doOnNext(professor -> {
                        ProfessorHelper aux = new ProfessorHelper(professor);

                        Flux<StudentProfessor> professorStudents = client
                                .get()
                                .uri("/relationship/professor/" + professor.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .retrieve()
                                .bodyToFlux(StudentProfessor.class);

                        professorStudents.flatMap(relationship -> {
                            int studentId= relationship.getStudent_id();

                            Mono<Student> studentDetails = client
                                    .get()
                                    .uri("/student/" + studentId)
                                    .accept(MediaType.APPLICATION_JSON)
                                    .retrieve()
                                    .bodyToMono(Student.class);

                            return studentDetails;
                        }).subscribe(student -> {aux.getProfessorStudents().add(student);});

                        helper.add(aux);
                    })
                    .subscribe();

            Thread.sleep(sleepTime);

            Collections.sort(helper);

            for (ProfessorHelper prof: helper) {
                writer.println("{Professor: " + prof.getProfessor().getName());

                writer.println("\tNumber of students: " + prof.getProfessorStudents().size());
                Collections.sort(prof.getProfessorStudents());
                for(Student student: prof.getProfessorStudents()) {
                    writer.println("\t{Student: " + student.getName() + "}");
                }

                writer.println("}");
            }

            writer.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private void exercise11(WebClient client, String filename, int sleepTime) {
        try {
            Flux<Student> studentsFlux = client
                    .get()
                    .uri("/student/")
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToFlux(Student.class);

            PrintWriter writer = new PrintWriter(filename);
            List<StudentHelper> helper = new ArrayList<>();

            studentsFlux
                    .doOnNext(student -> {
                        StudentHelper aux = new StudentHelper(student);

                        Flux<StudentProfessor> professorStudents = client
                                .get()
                                .uri("/relationship/student/" + student.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .retrieve()
                                .bodyToFlux(StudentProfessor.class);

                        professorStudents.flatMap(relationship -> {
                            int professorId = relationship.getProfessor_id();

                            Mono<Professor> professorDetails = client
                                    .get()
                                    .uri("/professor/" + professorId)
                                    .accept(MediaType.APPLICATION_JSON)
                                    .retrieve()
                                    .bodyToMono(Professor.class);

                            return professorDetails;
                        }).subscribe(professor -> {aux.getStudentProfessors().add(professor);});

                        helper.add(aux);
                    })
                    .subscribe();

            Thread.sleep(sleepTime);

            for (StudentHelper stud: helper) {
                writer.println("{" + stud.getStudent().toString());

                writer.println("\tProfessors: ");
                for(Professor professor: stud.getStudentProfessors()) {
                    writer.println("\t\t" + professor.getName());
                }

                writer.println("}");
            }

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

    private float longAverage(List<Long> array) {
        System.out.println(array);
        float average = array.stream().mapToInt(Long::intValue).sum();
        System.out.println(average);
        if (array.size() > 0) {
            average /= array.size();
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

    public ReactiveClientMultipleCalls() {
        myMain();
    }

    private void myMain() {
        long startTime = System.currentTimeMillis();

        WebClient client = WebClient.create(BASE_URL);

        exercise1(client, "1.txt", 2000);
        exercise2(client, "2.txt", 2000);
        exercise3(client, "3.txt", 2000);
        exercise4(client, "4.txt", 2000);
        exercise5(client, "5.txt", 2000);
        exercise6(client, "6.txt", 2000);
        exercise7(client, "7.txt", 2000);
        exercise8(client, "8.txt", 2000);
        exercise9(client, "9.txt", 2000);
        exercise10(client, "10.txt", 10000);
        exercise11(client, "11.txt", 10000);

        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        System.out.println("######### Operations Ended in "+ duration +"ms #########");
    }

    public static void main(String[] args) {
        ReactiveClientMultipleCalls client = new ReactiveClientMultipleCalls();
    }
}