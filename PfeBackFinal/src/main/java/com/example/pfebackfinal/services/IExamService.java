package com.example.pfebackfinal.services;

import com.example.pfebackfinal.presistence.entity.Courses;
import com.example.pfebackfinal.presistence.entity.Exams;

import java.util.List;

public interface IExamService {
    public Exams addExam(Exams exams);
    public List<Exams> getAllExams();
    public Exams getExamsById(String examsId);
    public Exams updateExams(Exams exams);
    public void deleteExams(String examsId);
}
