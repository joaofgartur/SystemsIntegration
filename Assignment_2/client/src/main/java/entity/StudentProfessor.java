package entity;

public class StudentProfessor {
    int id;

    int student_id;
    int professor_id;

    public StudentProfessor() {
    }

    public StudentProfessor(int student_id, int professor_id) {
        this.student_id = student_id;
        this.professor_id = professor_id;
    }

    public StudentProfessor(int id, int student_id, int professor_id) {
        this.id = id;
        this.student_id = student_id;
        this.professor_id = professor_id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public int getProfessor_id() {
        return professor_id;
    }

    public void setProfessor_id(int professor_id) {
        this.professor_id = professor_id;
    }

    @Override
    public String toString() {
        return "Student_Professor{" +
                "id=" + id +
                ", student_id=" + student_id +
                ", professor_id=" + professor_id +
                '}';
    }
}
