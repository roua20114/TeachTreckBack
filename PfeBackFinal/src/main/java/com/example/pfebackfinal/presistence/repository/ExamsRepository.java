package com.example.pfebackfinal.presistence.repository;

import com.example.pfebackfinal.presistence.entity.Exams;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExamsRepository  extends MongoRepository<Exams,String> {
}
