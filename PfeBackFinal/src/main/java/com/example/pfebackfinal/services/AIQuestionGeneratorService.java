package com.example.pfebackfinal.services;

import com.example.pfebackfinal.presistence.entity.QuestionResponse;
import com.example.pfebackfinal.presistence.entity.Questions;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AIQuestionGeneratorService {


        private static final String PYTHON_AI_MODEL_URL = "http://localhost:8000/ask_question/"; // Adjust URL as needed

        public List<Questions> generateQuestions(String domaine) {
            List<Questions> generatedQuestions = new ArrayList<>();
            try {
                // Create RestTemplate instance
                RestTemplate restTemplate = new RestTemplate();

                // Set up the request headers and body
                HttpHeaders headers = new HttpHeaders();
                headers.set("Content-Type", "application/json");

                // Log the domain we're sending
                System.out.println("Sending request to AI model with domain: " + domaine);

                // Correct the JSON key to "domaine" as per the Python FastAPI expectation
                String requestBody = "{ \"domaine\": \"" + domaine + "\" }";

                HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

                // Make the POST request to the AI model and receive the response
                ResponseEntity<QuestionResponse[]> response = restTemplate.exchange(
                        PYTHON_AI_MODEL_URL, HttpMethod.POST, requestEntity, QuestionResponse[].class
                );

                // Log the response status and body
                System.out.println("Received response from AI model: " + response.getStatusCode());
                System.out.println("Response body: " + Arrays.toString(response.getBody()));

                // Map the response to your Questions entity list
                if (response.getBody() != null) {
                    Arrays.stream(response.getBody()).forEach(qr -> {
                        Questions question = new Questions();
                        question.setQuestion(qr.getQuestion());
                        question.setCorrect_response(qr.getCorrect_response());
                        question.setQuestionId(qr.getQuestionId());
                        generatedQuestions.add(question);
                    });
                } else {
                    System.out.println("No questions returned from the AI model.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error occurred while calling AI model: " + e.getMessage());
            }

            return generatedQuestions;
        }



}
