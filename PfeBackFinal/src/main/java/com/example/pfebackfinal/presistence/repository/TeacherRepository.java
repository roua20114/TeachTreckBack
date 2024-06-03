package com.example.pfebackfinal.presistence.repository;

import com.example.pfebackfinal.presistence.entity.Courses;
import com.example.pfebackfinal.presistence.entity.Teacher;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends MongoRepository<Teacher,String> {
    Teacher findByEmail(String email);
    Optional<Teacher> findById(String id);
    Optional<Teacher> findByUsername(String username); ;
}
