package com.example.pfebackfinal.presistence.repository;

import com.example.pfebackfinal.presistence.entity.Comments;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentsRepository extends MongoRepository<Comments,String> {
}
