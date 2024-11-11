package com.example.pfebackfinal.presistence.repository;

import com.example.pfebackfinal.presistence.entity.Homework;
import com.example.pfebackfinal.presistence.entity.HomeworkSubmissionDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface HomeworkRepository extends MongoRepository<Homework,String> {
    List<Homework> findByClassroomId(String classroomId);
    List<HomeworkSubmissionDTO> findSubmissionsByStudentId(String studentId);

}
