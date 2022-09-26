package uc.mei.is;

import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Professor {
    @XmlAttribute
    private String id;

    @XmlElement
    private String name;

    @XmlElement
    private String birthDate;

    @XmlElement
    private String telephone;

    @XmlElement
    private String address;

    @XmlElement(name="students")
    private List<String> students;

    /*** Constructors ***/
    public Professor() {
    }

    public Professor(String id, String name, String birthDate, String telephone, String address) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.telephone = telephone;
        this.address = address;
        this.students = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getStudents() {
        return students;
    }

    public void setStudents(List<String> students) {
        this.students = students;
    }
}
