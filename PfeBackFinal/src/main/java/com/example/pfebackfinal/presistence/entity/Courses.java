package com.example.pfebackfinal.presistence.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;



@Getter
@Setter
@Document(collection = "courses")

public class Courses {
    @Id
    private String courseId;
    private String title;
    private String description;
    private String courseType;
    private String courseFile;
    @DBRef
    private Teacher teacher;



}
