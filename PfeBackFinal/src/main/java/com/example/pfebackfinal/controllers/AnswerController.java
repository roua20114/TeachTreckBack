package com.example.pfebackfinal.controllers;

import com.example.pfebackfinal.presistence.entity.*;
import com.example.pfebackfinal.services.AIModelEvaluator;
import com.example.pfebackfinal.services.AnswerService;
import com.example.pfebackfinal.services.ExamsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/answers")
@RequiredArgsConstructor
@CrossOrigin(origins="http://localhost:4200")
public class AnswerController {
    private final AnswerService answerService;
    private final AIModelEvaluator aiModelEvaluator;
    private final ExamsService examsService;
//    @Autowired
//    private  RestTemplate restTemplate;
//
//    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }




//    @PostMapping("/submit_answers")
//    public ResponseEntity<?> submitAnswers(@RequestBody AnswerRequest answerRequest) {
//        // Fetch the exam details
//        Exams exam = examsService.findByExamId(answerRequest.getExamId());
//
//        if (exam == null) {
//            return ResponseEntity.status(404).body("Exam not found");
//        }
//
//        // Call the service to evaluate the student's answers
//        EvaluationResponse evaluationResponse = aiModelEvaluator.evaluateStudentAnswers(
//                answerRequest.getExamId(),
//                answerRequest.getUser_responses()
//        );
//
//        // Return the evaluation result
//        return ResponseEntity.ok(evaluationResponse);
//    }
@PostMapping("/evaluate")
public Map<String, Object> evaluateExam(@RequestBody Map<String, Object> request) {
    String examId = (String) request.get("examId");
    String userId = (String) request.get("userId");
    Map<String, String> userResponses = (Map<String, String>) request.get("userResponses");

    // Call the service that connects to FastAPI
    return aiModelEvaluator.evaluateResponse(userResponses, examId,userId);
}

    @GetMapping("/evaluate/{examId}")
    public ResponseEntity<List<Map<String, Object>>> evaluateExam(@PathVariable String examId) {
        List<Answers> answers = answerService.getAnswersByExam(examId);
        Exams exam = examsService.findByExamId(examId);

        List<Map<String, Object>> questionsAndAnswers = new ArrayList<>();

        for (Answers answer : answers) {
            Map<String, Object> qa = new HashMap<>();
            qa.put("studentId", answer.getStudentId());
            qa.put("user_response", answer.getUserResponse()); // Map of questionId -> student's answer
            qa.put("exam", exam);

            questionsAndAnswers.add(qa);
        }

        return ResponseEntity.ok(questionsAndAnswers);
    }
    @GetMapping("/{examId}/questions")
    public ResponseEntity<List<Questions>> getExamQuestions(@PathVariable String examId) {
        List<Questions> questions = examsService.getExamQuestions(examId);
        return ResponseEntity.ok(questions);
    }
//    @GetMapping("/answers/{examId}")
//    public ResponseEntity<List<Answers>> getAnswersByExamId(@PathVariable String examId) {
//        List<Answers> answers = answerService.findAnswersByExamId(examId);
//        return ResponseEntity.ok(answers);
//    }
//    @PostMapping("/evaluate")
//    public ResponseEntity<?> submitAnswers(@RequestBody AnswerRequest answerRequest) {
//
//            // Fetch exam
//            Exams exam = examsService.findByExamId(answerRequest.getExamId());
//
//            if (exam == null) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Exam not found");
//            }
//
//            // Prepare the request for the AI model
//            Map<String, Object> aiRequest = new HashMap<>();
//            aiRequest.put("examId", answerRequest.getExamId());
//            aiRequest.put("user_responses", answerRequest.getUserResponses());  // Send responses as a list
//
//            // Call AI for evaluation
//            String aiServiceUrl = "http://localhost:8000/evaluate_response/";
//            ResponseEntity<Map> response = restTemplate.postForEntity(aiServiceUrl, aiRequest, Map.class);
//
//            if (response.getStatusCode() == HttpStatus.OK) {
//                return ResponseEntity.ok(response.getBody());
//            } else {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error evaluating answers");
//            }
//        }


    }
