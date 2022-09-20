package uc.mei.is;

import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Professor {
    @XmlAttribute
    String id;

    @XmlElement
    String name;

    @XmlElement
    String birthDate;

    @XmlElement
    String telephone;

    @XmlElement
    String address;

    @XmlElement
    List<Student> studentList;

    /*** Constructors ***/
    public Professor() {
    }

    public Professor(String id, String name, String birthDate, String telephone, String address) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.telephone = telephone;
        this.address = address;
        this.studentList = new ArrayList<Student>();
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

    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    @Override
    public String toString() {
        return "Professor{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", telephone='" + telephone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
