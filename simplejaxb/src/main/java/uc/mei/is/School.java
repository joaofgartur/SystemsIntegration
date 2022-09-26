package uc.mei.is;

import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="school")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(value = {Professor.class, Student.class})
public class School {
    @XmlElement(name="professor")
    List<Professor> professors;

    @XmlElement(name="student")
    List<Student> students;

    public School() {
        professors = new ArrayList<>();
        students = new ArrayList<>();
    }

    public List<Professor> getProfessors() {
        return professors;
    }

    public void setProfessors(List<Professor> professors) {
        this.professors = professors;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}