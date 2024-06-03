package com.example.pfebackfinal.services;

import com.example.pfebackfinal.presistence.entity.Exams;
import com.example.pfebackfinal.presistence.repository.ExamsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor

public class ExamsService implements IExamService{
    private final ExamsRepository examsRepository;
    @Override
    public Exams addExam(Exams exams) {
        return examsRepository.save(exams)  ;
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
}
