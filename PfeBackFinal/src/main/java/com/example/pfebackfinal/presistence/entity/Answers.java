package com.example.pfebackfinal.presistence.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Getter
@Setter
@Document(collection = "answers")
public class Answers {
    @Id
    private String answerId;
    private String questionId;
    private String examId;   // ID of the exam being answered
    private String studentId;  // ID of the student who answered
    private Map<String, String> userResponse; // A map of question to student answers
    private Integer score;
}
