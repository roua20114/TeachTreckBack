package com.example.pfebackfinal.services;

import com.example.pfebackfinal.presistence.entity.AnswerRequest;
import com.example.pfebackfinal.presistence.entity.Answers;
import com.example.pfebackfinal.presistence.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;

    public void submitAnswers(AnswerRequest answerRequest) {
        Answers answer = new Answers();
        answer.setExamId(answerRequest.getExamId());
        answer.setStudentId(answerRequest.getStudentId());
//        answer.setAnswers(answerRequest.getAnswers());

        // Save answer to the database
        answerRepository.save(answer);
    }
    public List<Answers> getAnswersByExam(String examId) {
        return answerRepository.findByExamId(examId);
    }
    public void saveAnswer(Answers answer) {
        answerRepository.save(answer);
    }

    public List<Answers> findByExamId(String examId) {
        return answerRepository.findByExamId(examId);
    }

}
