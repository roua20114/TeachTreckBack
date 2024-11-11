package com.example.pfebackfinal.presistence.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class PostInteractionDTO {
    private String postId;
    private String postContent;
    private Date interactionDate;
}
