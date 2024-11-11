package com.example.pfebackfinal.presistence.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document(collection = "classrooms")
public class Classroom {
    @Id
    private String id;
    private String name;
    private String code;
    private String link;
    private String creatorId;
    private List<Exams> exams;

    private List<String> studentEmails= new ArrayList<>();
    @DBRef
    private Teacher teacher;
    private boolean archived = false;
    @DBRef
    private List<Post> posts = new ArrayList<>();
}
