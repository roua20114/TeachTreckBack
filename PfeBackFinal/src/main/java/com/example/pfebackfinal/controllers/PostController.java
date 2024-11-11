package com.example.pfebackfinal.controllers;

import com.example.pfebackfinal.presistence.entity.*;
import com.example.pfebackfinal.presistence.repository.PostRepository;
import com.example.pfebackfinal.presistence.repository.UserRepository;
import com.example.pfebackfinal.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@RestController
@RequiredArgsConstructor

@RequestMapping("/private/post")


@CrossOrigin(origins="http://localhost:4200")
public class PostController {
    private final ClassroomService classroomService;
    private final UserRepository userRepository;
    private final ICourseService iCourseService;
    private final TeacherService teacherService;
    private final CourseService courseService;
    private  final PostService postService;
    private  final  UserService userService;

    private PostRepository postRepository;

    @PostMapping("/post/{classroomId}/{userId}")
    public ResponseEntity<Post> createPost(@PathVariable String classroomId,@PathVariable String userId, @RequestBody String content) {


        try {

            Post savedPost = classroomService.createPost(classroomId,userId,content);
            return new ResponseEntity<>(savedPost, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }


    }
    @GetMapping("/user/{teacherId}/posts")
    public ResponseEntity<List<Post>> getPostsByUser(@PathVariable String teacherId) {
        List<Post> posts = postService.getPostsByTeacherId((teacherId));
        if (posts != null && !posts.isEmpty()) {
            return ResponseEntity.ok(posts);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
    @PostMapping("/add/{postId}/{userId}")
    public ResponseEntity<Comment> addComment(@PathVariable String postId, @PathVariable String userId, @RequestBody String content) {
        try {
            Comment savedComment = postService.addCommentToPost(postId, userId, content);
            return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

//        String username = principal.getName();
//        UserEntity user = classroomService.findUserEntityByUsername(username);
//        Post post = postRepository.findById(postId).orElse(null);
//
//        Comment comment = new Comment();
//        comment.setContent(content);
//
//         // Save only the file name
//        comment.setUser(user);
//        comment.setCreatedAt(new Date());
//        comment.setPost(post);
//        post.getComments().add(comment);
//        postRepository.save(post);  // Save the updated post
//        return new ResponseEntity<>(comment, HttpStatus.CREATED);







    @GetMapping("/{classroomId}/posts")
    public ResponseEntity<List<Post>> getPostsByClassroom(@PathVariable String classroomId) {
        List<Post> posts = postService.getPostsByClassroom(classroomId);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable String postId) {
        List<Comment> comments = classroomService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }


//    @PostMapping("/{postId}/comments")
//    public ResponseEntity<Post> addCommentToPost(@PathVariable String postId, @RequestBody Comments commentDTO) {
//        Post post = postService.addCommentToPost(postId, commentDTO);
//        return ResponseEntity.ok(post);
//    }

    @GetMapping("/{postId}/commets")
    public ResponseEntity<List<Comment>> getCommentsForPost(@PathVariable String postId) {
        List<Comment> comments = postService.getCommentsForPost(postId);
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<Void> deleteCourse(
            @PathVariable String postId) {

        try {





            // Delete the course
            postService.deletePost(postId);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
