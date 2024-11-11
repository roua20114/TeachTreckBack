package com.example.pfebackfinal.presistence.repository;

import com.example.pfebackfinal.presistence.entity.Answers;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AnswerRepository extends MongoRepository<Answers, String> {
//    List<Answers> findByQuestions_Exam_ExamId(String examId);
    List<Answers> findByExamId(String examId);
}
