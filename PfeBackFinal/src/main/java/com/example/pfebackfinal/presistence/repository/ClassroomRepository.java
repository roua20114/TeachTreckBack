package com.example.pfebackfinal.presistence.repository;

import com.example.pfebackfinal.presistence.entity.Classroom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassroomRepository extends MongoRepository<Classroom, String> {
    Classroom findByCode(String code);
}
