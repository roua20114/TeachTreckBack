package com.example.pfebackfinal.presistence.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document(collection = "questions")
public class Questions {
    @Id
    private String questionId;
    private String question;
    private String correct_response;

    @DBRef
    private Exams exam;


}
