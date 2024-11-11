package com.example.pfebackfinal.presistence.repository;

import com.example.pfebackfinal.presistence.entity.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface StudentRepository extends MongoRepository<Student,String> {
    List<Student> findByEmailIn(List<String> emails);
}

