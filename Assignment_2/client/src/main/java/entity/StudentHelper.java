package entity;

import java.util.ArrayList;
import java.util.List;

public class StudentHelper {
    private Student student;
    private List<Professor> studentProfessors;

    public StudentHelper(Student student) {
        this.student = student;
        this.studentProfessors = new ArrayList<>();
    }

    public StudentHelper(Student student, List<Professor> studentProfessors) {
        this.student = student;
        this.studentProfessors = studentProfessors;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public List<Professor> getStudentProfessors() {
        return studentProfessors;
    }

    public void setStudentProfessors(List<Professor> studentProfessors) {
        this.studentProfessors = studentProfessors;
    }
}
