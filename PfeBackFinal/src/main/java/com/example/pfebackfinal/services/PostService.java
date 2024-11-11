package com.example.pfebackfinal.services;

import com.example.pfebackfinal.presistence.entity.Comment;
import com.example.pfebackfinal.presistence.entity.Comments;
import com.example.pfebackfinal.presistence.entity.Post;
import com.example.pfebackfinal.presistence.entity.UserEntity;
import com.example.pfebackfinal.presistence.repository.CommentRepository;
import com.example.pfebackfinal.presistence.repository.PostRepository;
import com.example.pfebackfinal.presistence.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostService {
    private  final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    public List<Post> getPostsByTeacherId(String teacherId) {
        return postRepository.findByTeacherId(teacherId); // Custom repository method
    }
    public List<Post> getPostsByClassroom(String classroomId) {
        return postRepository.findAllByClassroomId(classroomId);
    }
    public Comment addCommentToPost(String postId, String userId, String content) throws Exception {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (!postOptional.isPresent()) {
            throw new Exception("Post not found");
        }

        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new Exception("User not found");
        }

        Post post = postOptional.get();
        UserEntity user = userOptional.get();

        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setUserId(userId);
        comment.setUsername(user.getUsername());
        comment.setContent(content);
        comment.setCreatedAt(new Date());

        // Save the comment
        Comment savedComment = commentRepository.save(comment);

        // Add the comment to the post
        post.getComments().add(savedComment);
        postRepository.save(post);

        return savedComment;
    }
    public List<Comment> getCommentsForPost(String postId) {
        return commentRepository.findByPostId(postId);
    }
    public void deletePost(String postId){
        postRepository.deleteById(postId);
    }


}
