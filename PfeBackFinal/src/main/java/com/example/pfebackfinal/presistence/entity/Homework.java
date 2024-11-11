package com.example.pfebackfinal.presistence.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Document(collection = "homeworks")
public class Homework {
    @Id
    private String id;
    private String title;
    private String description;
    private Date dueDate;
    private String teacherId;
    private String classroomId;
    private Date createdAt = new Date();
    private String studentId;

}
