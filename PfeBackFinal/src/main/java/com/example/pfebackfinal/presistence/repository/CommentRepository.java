package com.example.pfebackfinal.presistence.repository;

import com.example.pfebackfinal.presistence.entity.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment,String> {
    List<Comment> findByPostId(String postId);

}
