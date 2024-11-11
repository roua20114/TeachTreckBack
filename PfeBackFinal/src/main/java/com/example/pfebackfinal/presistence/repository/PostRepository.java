package com.example.pfebackfinal.presistence.repository;

import com.example.pfebackfinal.presistence.entity.Post;
import com.example.pfebackfinal.presistence.entity.PostInteractionDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post,String> {
    List<Post> findByClassroomId(String classroomId);

    List<Post> findByTeacherId(String teacherId);
    List<Post> findAllByClassroomId(String classroomId);

}
