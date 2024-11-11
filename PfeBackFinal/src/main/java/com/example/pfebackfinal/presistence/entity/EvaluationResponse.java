package com.example.pfebackfinal.presistence.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EvaluationResponse {
    private List<EvaluationResult> evaluations;
    private float totalScore;
}
