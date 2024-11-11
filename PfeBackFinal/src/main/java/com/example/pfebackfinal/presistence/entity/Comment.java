package com.example.pfebackfinal.presistence.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
@Getter
@Setter
@Document(collection = "comments")
public class Comment {
    @Id
    private String id;
    private String postId;
    private String content;
    private String userId;
    private String username;
    private Date createdAt;
}
