package com.example.pfebackfinal.presistence.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.userdetails.User;

@Getter
@Setter
@Document(collection = "comments")
public class Comments {
    @Id
    private String commentId;
    private String content;
    private User user;

}
