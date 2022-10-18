package com.example.server.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Professor {
    @Id
    private Long id;
    private String name;

    public Professor(Long id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public String toString() {
        return "Professor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
