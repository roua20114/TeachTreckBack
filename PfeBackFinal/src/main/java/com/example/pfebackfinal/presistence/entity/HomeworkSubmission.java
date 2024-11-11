package com.example.pfebackfinal.presistence.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Document(collection = "homework_submissions")
public class HomeworkSubmission {
    @Id
    private String id;

    @DBRef
    private Student student;

    @DBRef
    private Classroom classroom;

    private String homeworkContent;

    private Date submittedAt;
}
