package com.example.pfebackfinal.presistence.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;
@Setter
@Getter
@Document(collection = "answerstudent")
public class AnswerRequest {
    private String examId;
    private String studentId;
    private String questionId;
    private Map<String, String> user_responses;

}
