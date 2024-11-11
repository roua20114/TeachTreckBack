package com.example.pfebackfinal.presistence.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Document(collection = "courses")

public class Courses {
    @Id
    private String courseId;
    private String title;
    private String description;
    private String courseType;
    private String filePath;
    @DBRef
    private Teacher teacher;
    private List<Comment> comments = new ArrayList<>();
    @DBRef
    private Classroom classroom;
}
