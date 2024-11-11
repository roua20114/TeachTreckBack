package com.example.pfebackfinal.presistence.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document(collection = "exams")
public class Exams {
    @Id
    private String examId;
    private String title;
    private String description;
    private String domaine;

    @DBRef
    private Classroom classroom;  // Reference to Classroom entity

    private List<Questions> question;


}
