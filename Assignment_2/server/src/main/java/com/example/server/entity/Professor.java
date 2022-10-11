package com.example.server.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Professor {

    @Id
    private Long id;
    private String name;
}
