package com.example.pfebackfinal.presistence.repository;

import com.example.pfebackfinal.presistence.entity.Classroom;
import com.example.pfebackfinal.presistence.entity.HomeworkSubmission;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface HomeworkSubmissionRepository extends MongoRepository<HomeworkSubmission,String> {
    List<HomeworkSubmission> findByClassroom(Classroom classroom);;
}
