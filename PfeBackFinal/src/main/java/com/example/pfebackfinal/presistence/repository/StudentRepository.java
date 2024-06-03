package com.example.pfebackfinal.presistence.repository;

import com.example.pfebackfinal.presistence.entity.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends MongoRepository<Student,String> {
}

