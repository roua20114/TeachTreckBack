package com.example.pfebackfinal.controllers;


import com.example.pfebackfinal.presistence.entity.Exams;
import com.example.pfebackfinal.services.ExamsService;
import com.example.pfebackfinal.services.IExamService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exams")

@AllArgsConstructor
@CrossOrigin(origins="http://localhost:4200")

public class ExamsController {
    private  final IExamService iExamService;
    private final ExamsService examService;

    @PostMapping("/addExam")
    public ResponseEntity<Exams> saveExams(@RequestBody Exams exams){
        Exams exam= iExamService.addExam(exams);
        return ResponseEntity.ok(exam) ;
    }

    @GetMapping("/examList")
    public ResponseEntity<List<Exams>> getAllExam(){
        return ResponseEntity.ok(iExamService.getAllExams());
    }
    @GetMapping("/{examId}")
    public ResponseEntity<Exams> getCourseById(@PathVariable("examId") String examId){
        Exams exam= iExamService.getExamsById(examId);
        if (exam==null){
            return ResponseEntity.notFound().build();
        }else {
            return ResponseEntity.ok(exam);
        }


    }
    @PutMapping("/updateExams")
    public ResponseEntity<Exams> updateCourse(@RequestBody Exams exams){
        Exams exam= iExamService.updateExams(exams);
        return ResponseEntity.ok(exam);
    }
    @DeleteMapping("/deleteExams/{examId}")
    public void deleteCourse(@PathVariable String examId){
        iExamService.deleteExams(examId);
    }
    @GetMapping("/detail/{examId}")
    public ResponseEntity<?> getExamDetails(@PathVariable String examId) {
        try {
            Exams exam = examService.getExamById(examId);
            if (exam != null) {
                return ResponseEntity.ok(exam);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Exam not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching exam details");
        }
    }

 }
