package com.example.pfebackfinal.controllers;

import com.example.pfebackfinal.presistence.entity.*;
import com.example.pfebackfinal.services.AIQuestionGeneratorService;
import com.example.pfebackfinal.services.ClassroomService;
import com.example.pfebackfinal.services.ExamsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/exams")

public class ExamController {
    private static final String PYTHON_AI_EVALUATION_URL = "http://localhost:8000/evaluate_response/";
    private final ExamsService examService;
    private final ClassroomService classroomService;
    private final AIQuestionGeneratorService aiQuestionGeneratorService;
    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/add-to-classroom/{classroomId}")
    public ResponseEntity<?> addExamToClassroom(@PathVariable String classroomId, @RequestBody Exams exam) {
        if (exam == null) {
            return ResponseEntity.badRequest().body("Exam object is missing");
        }
        System.out.println("Received Exam: " + exam.toString());
        Classroom classroom = classroomService.addExamToClassroom(classroomId, exam);


        if (classroom != null) {
            return ResponseEntity.ok(classroom);
        } else {
            return ResponseEntity.status(404).body("Classroom not found");
        }
    }

    @GetMapping
    public ResponseEntity<List<Exams>> getAllExams() {
        return new ResponseEntity<>(examService.getAllExams(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Exams> getExamById(@PathVariable String id) {
        Exams exam = examService.getExamsById(id);
        return exam != null ? ResponseEntity.ok(exam) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExam(@PathVariable String id) {
        examService.deleteExams(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/create/{classroomId}")
    public ResponseEntity<?> createExam(@RequestBody ExamRequest examRequest, @PathVariable String classroomId) {
        // Create the exam entity
        Exams exam = new Exams();
        exam.setTitle(examRequest.getTitle());
        exam.setDescription(examRequest.getDescription());
        exam.setDomaine(examRequest.getDomaine());

        // Fetch the classroom and set it in the exam
        Classroom classroom = classroomService.findById(classroomId);
        if (classroom == null) {
            return ResponseEntity.status(404).body("Classroom not found");
        }
        exam.setClassroom(classroom);

        // Call AI service to generate questions based on the domain
        List<Questions> generatedQuestions = aiQuestionGeneratorService.generateQuestions(examRequest.getDomaine());
        exam.setQuestion(generatedQuestions);

        // Save the exam
        examService.saveExam(exam);

        return ResponseEntity.ok(exam);
    }
    @PostMapping("/evaluate/{examId}")
    public ResponseEntity<?> evaluateExamResponses(@PathVariable String examId, @RequestBody Map<String, String> studentResponses) {
        try {
            // Prepare the request for the FastAPI evaluation
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("examId", examId);
            requestBody.put("user_responses", studentResponses); // Map of questionId -> studentAnswer

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            // Send request to FastAPI for evaluation
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    PYTHON_AI_EVALUATION_URL, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            // Return the evaluation result to the frontend
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error evaluating exam responses: " + e.getMessage());
        }
    }



//    @PostMapping("/evaluate")
//    public ResponseEntity<String> evaluateExam(@RequestBody EvaluateRequest evaluateRequest) {
//        examService.evaluateExam(evaluateRequest.getExamId());
//        return ResponseEntity.ok("Evaluation completed.");
//
//    }
    @GetMapping("/allexamst/{classroomId}")
    public ResponseEntity<List<Exams>> getExamsByClassroom(@PathVariable String classroomId) {
        List<Exams> exams = examService.getExamsByClassroomId(classroomId);
        return ResponseEntity.ok(exams);
    }

}
