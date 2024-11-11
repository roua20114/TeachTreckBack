package com.example.pfebackfinal.presistence.repository;

import com.example.pfebackfinal.presistence.entity.Exams;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ExamsRepository  extends MongoRepository<Exams,String> {
    List<Exams> findByClassroomId(String classroomId);
    Optional<Exams> findById(String examId);
}
