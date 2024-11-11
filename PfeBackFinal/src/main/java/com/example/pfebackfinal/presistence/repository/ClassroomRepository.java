package com.example.pfebackfinal.presistence.repository;

import com.example.pfebackfinal.presistence.entity.Classroom;
import com.example.pfebackfinal.presistence.entity.Courses;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface ClassroomRepository extends MongoRepository<Classroom, String> {
    Classroom findByCode(String code);
    List<Classroom> findByTeacherId(String teacherId);
    List<Classroom> findByTeacherIdAndArchivedFalse(String teacherId);
    Optional<Classroom> findByIdAndTeacherId(String id, String teacherId);
    List<Classroom> findByStudentEmailsContaining(String studentEmail);
    List<Classroom> findByNameContainingIgnoreCase(String keyword);
    Optional<Classroom> findById(String classroomId);



}
