package org.example;

import entity.*;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Semaphore;


public class ReactiveClientThreads {
    private final String BASE_URL = "http://localhost:8080";
    private void exercise1(Flux<Student> studentsFlux, Semaphore semaphore) {
        try {
            PrintWriter writer = new PrintWriter("1.txt");
            studentsFlux
                    .publishOn(Schedulers.boundedElastic())
                    .doOnComplete(() -> {
                        writer.close();
                        semaphore.release(1);
                    })
                    .subscribe(s -> writer.println("{Name: " + s.getName() + ", Birthdate: " + s.getBirthdate() + "}"));

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private void exercise2(Flux<Student> studentsFlux, Semaphore semaphore) {
        try {
            PrintWriter writer = new PrintWriter("2.txt");
            studentsFlux
                    .publishOn(Schedulers.boundedElastic())
                    .count()
                    .subscribe(count -> {
                        writer.println("{Count: " + count + "}");
                        writer.close();
                        semaphore.release(1);
                    });
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private void exercise3(Flux<Student> studentsFlux, Semaphore semaphore) {
        try {
            PrintWriter writer = new PrintWriter("3.txt");
            studentsFlux
                    .publishOn(Schedulers.boundedElastic())
                    .filter(student -> student.getCredits() < 180)
                    .count()
                    .subscribe(count -> {
                        writer.println("{Number of active students: " + count + "}");
                        writer.close();
                        semaphore.release(1);
                    });
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private void exercise4(Flux<Student> studentsFlux, Semaphore semaphore) {
        try {
            PrintWriter writer = new PrintWriter("4.txt");
            studentsFlux
                    .publishOn(Schedulers.boundedElastic())
                    .map(Student::getCredits)
                    .reduce(Integer::sum)
                    .subscribe(sum -> {
                        writer.println("{Number of completed courses: " + (sum / 6) + "}");
                        writer.close();
                        semaphore.release(1);
                    });
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private void exercise5(Flux<Student> studentsFlux, Semaphore semaphore) {
        try {
            PrintWriter writer = new PrintWriter("5.txt");
            studentsFlux
                    .publishOn(Schedulers.boundedElastic())
                    .filter(student -> student.getCredits() >= 120 && student.getCredits() < 180)
                    .sort(Comparator.comparing(Student::getCredits))
                    .doOnComplete(() -> {
                        writer.close();
                        semaphore.release(1);
                    })
                    .subscribe(student -> {writer.println("{" + student.getName() + ", Credits: " + student.getCredits() + "}");});
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private void exercise6(Flux<Student> studentsFlux, Semaphore semaphore) {
        try {
            PrintWriter writer = new PrintWriter("6.txt");
            studentsFlux
                    .publishOn(Schedulers.boundedElastic())
                    .map(Student::getAverageGrade)
                    .buffer()
                    .subscribe(grades -> {
                        writer.println("{Average: " + average(grades) + ", Std: " + standardDeviation(grades) + "}");
                        writer.close();
                        semaphore.release(1);
                    });
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private void exercise7(Flux<Student> studentsFlux, Semaphore semaphore) {
        try {
            PrintWriter writer = new PrintWriter("7.txt");
            studentsFlux
                    .publishOn(Schedulers.boundedElastic())
                    .filter(student -> student.getCredits() == 180)
                    .map(Student::getAverageGrade)
                    .buffer()
                    .subscribe(grades -> {
                        writer.println("{Average: " + average(grades) + ", Std: " + standardDeviation(grades) + "}");
                        writer.close();
                        semaphore.release(1);
                    });
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private void exercise8(Flux<Student> studentsFlux, Semaphore semaphore) {
        try {
            PrintWriter writer = new PrintWriter("8.txt");
            studentsFlux
                    .publishOn(Schedulers.boundedElastic())
                    .reduce((a, b) -> {
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

                    })
                    .subscribe(student -> {
                        writer.println("{Name: " + student.getName() + "}");
                        writer.close();
                        semaphore.release(1);
                    });
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private void exercise9(WebClient client, Flux<Student> studentsFlux, Semaphore semaphore) {
        try {
            PrintWriter writer = new PrintWriter("9.txt");

            studentsFlux
                    .publishOn(Schedulers.boundedElastic())
                    .flatMap(student -> {
                        Flux<StudentProfessor> studentProfessors = client
                                .get()
                                .uri("/relationship/student/" + student.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .retrieve()
                                .bodyToFlux(StudentProfessor.class);

                        return studentProfessors.count();
                    })
                    .buffer()
                    .subscribe(array -> {
                        writer.println("{Average number of professors per student: " + longAverage(array) + "}");
                        writer.close();
                        semaphore.release(1);
                    });
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    private void exercise10(WebClient client, Flux<Professor> professorsFlux, Semaphore semaphore) {
        try {
            PrintWriter writer = new PrintWriter("10.txt");
            List<ProfessorHelper> helper = new ArrayList<>();

            Semaphore helperSemaphore = new Semaphore(0);

            professorsFlux
                    .publishOn(Schedulers.boundedElastic())
                    .doOnComplete(() -> {
                        helperSemaphore.release(1);
                    })
                    .doOnNext(professor -> {

                        try {
                            ProfessorHelper aux = new ProfessorHelper(professor);
                            Semaphore another = new Semaphore(0);

                            Flux<StudentProfessor> professorStudents = client
                                    .get()
                                    .uri("/relationship/professor/" + professor.getId())
                                    .accept(MediaType.APPLICATION_JSON)
                                    .retrieve()
                                    .bodyToFlux(StudentProfessor.class);

                            professorStudents
                                    .doOnComplete(() -> {another.release(1);})
                                    .flatMap(relationship -> {
                                        int studentId = relationship.getStudent_id();

                                        Mono<Student> studentDetails = client
                                                .get()
                                                .uri("/student/" + studentId)
                                                .accept(MediaType.APPLICATION_JSON)
                                                .retrieve()
                                                .bodyToMono(Student.class);

                                        return studentDetails;


                                    }).subscribe(student -> {aux.getProfessorStudents().add(student);});

                            another.acquire(1);
                            helper.add(aux);
                        } catch (Exception e) {
                            System.out.println(e.toString());
                        }
                    })
                    .subscribe();

            //Thread.sleep(1000);
            helperSemaphore.acquire(1);

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
            semaphore.release(1);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private void exercise11(WebClient client, Flux<Student> studentsFlux, Semaphore semaphore) {
        try {
            PrintWriter writer = new PrintWriter("11.txt");
            List<StudentHelper> helper = new ArrayList<>();
            Semaphore helperSemaphore = new Semaphore(0);

            studentsFlux
                    .publishOn(Schedulers.boundedElastic())
                    .doOnComplete(() -> {
                        helperSemaphore.release(1);
                    })
                    .doOnNext(student -> {
                        try {
                            StudentHelper aux = new StudentHelper(student);
                            Semaphore another = new Semaphore(0);

                            Flux<StudentProfessor> professorStudents = client
                                    .get()
                                    .uri("/relationship/student/" + student.getId())
                                    .accept(MediaType.APPLICATION_JSON)
                                    .retrieve()
                                    .bodyToFlux(StudentProfessor.class);

                            professorStudents
                                    .doOnComplete(() -> {another.release(1);})
                                    .flatMap(relationship -> {
                                        int professorId= relationship.getProfessor_id();

                                        Mono<Professor> professorDetails = client
                                                .get()
                                                .uri("/professor/" + professorId)
                                                .accept(MediaType.APPLICATION_JSON)
                                                .retrieve()
                                                .bodyToMono(Professor.class);

                                        return professorDetails;
                                    }).subscribe(professor -> {aux.getStudentProfessors().add(professor);});

                            another.acquire(1);
                            helper.add(aux);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .subscribe();

            helperSemaphore.acquire(1);

            for (StudentHelper stud: helper) {
                writer.println("{" + stud.getStudent().toString());

                writer.println("\tProfessors: ");
                for(Professor professor: stud.getStudentProfessors()) {
                    writer.println("\t\t" + professor.getName());
                }

                writer.println("}");
            }

            writer.close();
            semaphore.release(1);
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

    public ReactiveClientThreads() {
        myMain();
    }

    private void myMain() {
        ArrayList<Long> executionTimes = new ArrayList<Long>();

        for (int i = 0; i < 1; i++) {
            long startTime = System.currentTimeMillis();
            WebClient client = WebClient.create(BASE_URL);

            try {
                Flux<Student> studentsFlux = client
                        .get()
                        .uri("/student/")
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToFlux(Student.class);

                Flux<Professor> professorsFlux = client
                        .get()
                        .uri("/professor/")
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToFlux(Professor.class);

                Semaphore semaphore = new Semaphore(0);

                exercise1(studentsFlux, semaphore);
                exercise2(studentsFlux, semaphore);
                exercise3(studentsFlux, semaphore);
                exercise4(studentsFlux, semaphore);
                exercise5(studentsFlux, semaphore);
                exercise6(studentsFlux, semaphore);
                exercise7(studentsFlux, semaphore);
                exercise8(studentsFlux, semaphore);
                exercise9(client, studentsFlux, semaphore);
                exercise10(client, professorsFlux, semaphore);
                exercise11(client, studentsFlux, semaphore);

                semaphore.acquire(11);

                long endTime = System.currentTimeMillis();
                long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
                executionTimes.add(duration);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }

        System.out.println("######### Tests execution time (ms) #########");
        for (Long time: executionTimes) {
            System.out.println(time);
        }

    }

    public static void main(String[] args) {
        ReactiveClientThreads client = new ReactiveClientThreads();
    }
}