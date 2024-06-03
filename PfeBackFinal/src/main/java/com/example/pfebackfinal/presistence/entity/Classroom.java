package com.example.pfebackfinal.presistence.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document(collection = "classrooms")
public class Classroom {
    @Id
    private String id;
    private String name;
    private String code;
    private List<String> studentEmails;

}
