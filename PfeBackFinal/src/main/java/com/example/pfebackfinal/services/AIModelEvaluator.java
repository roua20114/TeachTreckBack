package com.example.pfebackfinal.services;

import com.example.pfebackfinal.presistence.entity.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service

public class AIModelEvaluator {

//    private static final String PYTHON_AI_EVALUATION_URL = "http://localhost:8000/evaluate_response/";
//
//    public EvaluationResponse evaluateStudentAnswers(String examId, Map<String, String> user_responses) {
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-Type", "application/json");
//
//        Map<String, Object> requestBody = new HashMap<>();
//        requestBody.put("examId", examId);
//        requestBody.put("user_responses", user_responses);
//
//        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
//
//        // Call the AI model API to evaluate the answers
//        ResponseEntity<EvaluationResponse> responseEntity = restTemplate.exchange(
//                PYTHON_AI_EVALUATION_URL,
//                HttpMethod.POST,
//                requestEntity,
//                EvaluationResponse.class
//        );
//
//        // Return the evaluation response received from the AI model
//        return responseEntity.getBody();
//    }
@Autowired
    private  RestTemplate restTemplate;


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public Map<String, Object> evaluateResponse(Map<String, String> userResponses, String examId,String userId) {
        String apiUrl = "http://localhost:8000/evaluate_response/";

        // Prepare request body
        Map<String, Object> requestBody = Map.of(
                "user_responses", userResponses,
                "examId", examId,
                "userId", userId
        );

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // Create the request
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Make the HTTP POST request to the FastAPI service
        ResponseEntity<Map> responseEntity = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                requestEntity,
                Map.class
        );

        // Get the response (evaluations and total score)
        return responseEntity.getBody();
    }
}

