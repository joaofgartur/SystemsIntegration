package com.example.server.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.sql.Date;

@Data
public class Student {
    @Id
    private Long id;
    private String name;
    private Date birthdate;
    private int credits;
    private int averageGrade;

    public Student(Long id, String name, Date birthdate, int credits, int averageGrade) {
        this.id = id;
        this.name = name;
        this.birthdate = birthdate;
        this.credits = credits;
        this.averageGrade = averageGrade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getAverageGrade() {
        return averageGrade;
    }

    public void setAverageGrade(int averageGrade) {
        this.averageGrade = averageGrade;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthdate=" + birthdate +
                ", credits=" + credits +
                ", avgGrade=" + averageGrade +
                '}';
    }
}
