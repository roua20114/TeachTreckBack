package com.example.pfebackfinal.presistence.repository;

import com.example.pfebackfinal.presistence.entity.Courses;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CoursesRepository extends MongoRepository<Courses,String> {
    List<Courses> findByTeacherId(String teacherId);
//    Optional<Courses> updateByTeacherId(String teacherId);


}
