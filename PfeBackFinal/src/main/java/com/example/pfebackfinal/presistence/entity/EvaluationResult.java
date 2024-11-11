package com.example.pfebackfinal.presistence.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvaluationResult {
    private String questionId;
    private String user_response;
    private String correct_response;
    private float score;
    private float scoreRatio;
}
