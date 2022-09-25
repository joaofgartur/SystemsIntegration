package uc.mei.is;

import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="professors")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(value = {Professor.class, Student.class})
public class ProfessorsContainer {
    @XmlElement(name="professor")
    List<Professor> professors;

    public ProfessorsContainer() {
        professors = new ArrayList<>();
    }

    public List<Professor> getProfessors() {
        return professors;
    }

    public void setProfessors(List<Professor> professors) {
        this.professors = professors;
    }
}
