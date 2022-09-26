package uc.mei.is;

import jakarta.xml.bind.annotation.*;
import uc.mei.is.ProtocolBufferClasses.ProtoStudent;

import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Student {

    @XmlAttribute
    private String id;

    @XmlElement
    private String name;

    @XmlElement
    private String telephone;

    @XmlElement
    private String gender;

    @XmlElement
    private String birthDate;

    @XmlElement
    private String registrationDate;

    @XmlElement
    private String address;

    @XmlElement
    private String professor;

    /*** Constructors ***/
    public Student() {
    }

    public Student(String id, String name, String telephone, String gender, String birthDate,
                   String registrationDate, String address) {
        this.id = id;
        this.name = name;
        this.telephone = telephone;
        this.gender = gender;
        this.birthDate = birthDate;
        this.registrationDate = registrationDate;
        this.address = address;
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public ProtoStudent convertToProto() {
        ProtoStudent.Builder student =  ProtoStudent.newBuilder();

        student.setId(this.id)
                .setName(this.name)
                .setTelephone(this.telephone)
                .setGender(this.gender)
                .setBirthDate(this.birthDate)
                .setRegistrationDate(this.registrationDate)
                .setAddress(this.address);

        if(this.professor != null) {
            student.setProfessor(this.professor);
        }

        return student.build();
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", telephone='" + telephone + '\'' +
                ", gender='" + gender + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", registrationDate='" + registrationDate + '\'' +
                ", address='" + address + '\'' +
                ", professor='" + professor + '\'' +
                '}';
    }
}
