package entity;

import java.util.ArrayList;
import java.util.List;

public class ProfessorHelper implements Comparable {
    private Professor professor;
    private List<Student> professorStudents;

    public ProfessorHelper (Professor professor) {
        this.professor = professor;
        this.professorStudents = new ArrayList<>();
    }

    public ProfessorHelper(Professor professor, List<Student> professorStudents) {
        this.professor = professor;
        this.professorStudents = professorStudents;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public List<Student> getProfessorStudents() {
        return professorStudents;
    }

    public void setProfessorStudents(List<Student> professorStudents) {
        this.professorStudents = professorStudents;
    }

    @Override
    public int compareTo(Object object) {
        int compareValue =((ProfessorHelper) object).getProfessorStudents().size();
        return compareValue - this.getProfessorStudents().size();
    }
}
