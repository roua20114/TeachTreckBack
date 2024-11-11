package com.example.pfebackfinal.presistence.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Document(collection = "posts")
public class Post {
    @Id
    private String id;

    private String content;
    private String teacherId;
    private String username;
    private String classroomId;
    private Date createdAt = new Date();
    private String userId;;
    @DBRef
    private List<Comment> comments = new ArrayList<>();
}
