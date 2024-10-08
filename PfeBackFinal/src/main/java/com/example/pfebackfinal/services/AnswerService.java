package com.example.pfebackfinal.services;

import com.example.pfebackfinal.presistence.entity.Answers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ExamService {
    private final ExamRepository examRepository;

    public void submitAnswers(List<Answers> answers) {
        answerRepository.saveAll(answers);
    }

    // Get answers for a specific exam
    public List<Answers> getAnswersByExamId(String examId) {
        return answerRepository.findByQuestions_ExamId(examId);
    }
}
}
