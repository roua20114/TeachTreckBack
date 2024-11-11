package com.example.pfebackfinal.services;

import com.example.pfebackfinal.presistence.entity.*;
import com.example.pfebackfinal.presistence.repository.AnswerRepository;
import com.example.pfebackfinal.presistence.repository.ExamsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor

public class ExamsService implements IExamService {
    private final ExamsRepository examsRepository;
    private final AnswerRepository answerRepository;
    private final AIQuestionGeneratorService aiQuestionGeneratorService;
//    private final AIModelEvaluator aiModelEvaluator;


    @Override
    public Exams addExam(Exams exams) {
        return examsRepository.save(exams);
    }

    @Override
    public List<Exams> getAllExams() {
        return examsRepository.findAll();
    }

    @Override
    public Exams getExamsById(String examsId) {
        return examsRepository.findById(examsId).get();
    }

    @Override
    public Exams updateExams(Exams exams) {
        return examsRepository.save(exams);
    }

    @Override
    public void deleteExams(String examsId) {
        examsRepository.deleteById(examsId);

    }

    public Questions addQuestion(String examId, Questions questions) {
        Exams exam = examsRepository.findById(examId).orElseThrow(() -> new RuntimeException("Exam not found"));
        exam.getQuestion().add(questions);
        examsRepository.save(exam);
        return questions;
    }

    public Exams getExamById(String examId) {
        return examsRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
    }

    public List<Exams> getExamsByClassroomId(String classroomId) {
        return examsRepository.findByClassroomId(classroomId);
    }
    public Exams createExam(ExamRequest examRequest) {
        Exams exam = new Exams();
        exam.setTitle(examRequest.getTitle());
        exam.setDescription(examRequest.getDescription());
        exam.setDomaine(examRequest.getDomaine());
        // Set classroom reference using classroomId
        // exam.setClassroom(...);

        // Generate AI-based questions
        List<Questions> questions = aiQuestionGeneratorService.generateQuestions(examRequest.getDomaine());
        exam.setQuestion(questions);

        return examsRepository.save(exam);
    }

//    public void submitAnswers(AnswerRequest answerRequest) {
//        Answers answer = new Answers();
//        answer.setExamId(answerRequest.getExamId());
//        answer.setStudentId(answerRequest.getStudentId());
//        answer.setAnswers(answerRequest.getAnswers());
//
//        answerRepository.save(answer);
//    }

//    public void evaluateExam(String examId) {
//        List<Answers> answers = answerRepository.findByExamId(examId);
//        Exams exam = examsRepository.findById(examId).orElseThrow(() -> new RuntimeException("Exam not found"));
//
//        for (Answers answer : answers) {
//            int score = aiModelEvaluator.evaluateAnswers(exam, answer);
//            answer.setScore(score);
//            answerRepository.save(answer);  // Update the answer with the score
//        }
//    }
    public void saveExam(Exams exam) {
        // Save the exam to the repository
        examsRepository.save(exam);
    }
    public Exams findByExamId(String examId) {
        return examsRepository.findById(examId).orElseThrow(() -> new RuntimeException("Exam not found"));
    }
    public List<Questions> getExamQuestions(String examId) {
        Exams exam = examsRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        return exam.getQuestion();  // Return the list of questions for the exam
    }
}
