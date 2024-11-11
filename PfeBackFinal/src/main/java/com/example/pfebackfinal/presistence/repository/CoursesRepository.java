package com.example.pfebackfinal.presistence.repository;

import com.example.pfebackfinal.presistence.entity.Courses;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CoursesRepository extends MongoRepository<Courses,String> {
    List<Courses> findByTeacher_Id(String teacherId);
    List<Courses> findByClassroomId(String classroomId);


    List<Courses> findByTitleContainingIgnoreCase(String keyword);
    List<Courses> findByTeacherIdAndClassroomId(String teacherId, String classroomId);




}
