package com.example.pfebackfinal.services;

import com.example.pfebackfinal.exception.ApplicationException;
import com.example.pfebackfinal.model.internal.RoleEnumeration;
import com.example.pfebackfinal.presistence.entity.*;
import com.example.pfebackfinal.presistence.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassroomService implements IClassroomService {

    private final ClassroomRepository classroomRepository;
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final PostRepository postRepository;
    private final HomeworkRepository homeworkRepository;
    private final CommentRepository commentRepository;

    @Override
    public Classroom createClassroom(Classroom classroom) {
        Teacher teacher = classroom.getTeacher();
        if (teacher == null || !teacherRepository.existsById(teacher.getId())) {
            throw new RuntimeException("Teacher not found for the given course");
        }
        String generatedCode = generateRandomCode(6);

        classroom.setCode(generatedCode);
        classroom.setLink(generateRandomUrl()); // Assuming you have a method to generate a random URL
        return classroomRepository.save(classroom);
    }
    private String generateRandomCode(int length) {
        // Generate a random alphanumeric code of specified length
        return RandomStringUtils.randomAlphanumeric(length).toUpperCase();
    }

    private String generateRandomUrl() {
        // Logic to generate a random URL, for example:
        return "https://classroom.com/" + UUID.randomUUID().toString();
    }


    @Override
    public Classroom addPupilToClassroom(String classroomId, String email) {
        Classroom classroom = classroomRepository.findById(classroomId).get();
        if (classroom != null) {
            // Check if the user with the given email exists
            Optional<UserEntity> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();
                if (user.getRole() == RoleEnumeration.STUDENT) {
                    classroom.getStudentEmails().add(email);
                    return classroomRepository.save(classroom);
                } else {
                    // Handle the case where the user doesn't have the STUDENT role
                    // You can throw an exception or return an appropriate response
                    throw new IllegalArgumentException("User does not have STUDENT role.");
                }
            } else {
                // Handle the case where the user doesn't exist
                // For example:
                throw new IllegalArgumentException("User not found.");
            }
        }
        return null;
    }

    @Override
    public void addStudentsToClassroom(String classroomId, List<String> studentEmails) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found with id: " + classroomId));

        List<Student> studentsToAdd = studentRepository.findByEmailIn(studentEmails);
        List<String> existingStudentEmails = studentsToAdd.stream()
                .map(Student::getEmail)
                .collect(Collectors.toList());

        // Validate if all student emails provided exist in the database
        List<String> nonExistingEmails = studentEmails.stream()
                .filter(email -> !existingStudentEmails.contains(email))
                .collect(Collectors.toList());

        if (!nonExistingEmails.isEmpty()) {
            throw new RuntimeException("Students not found in database with emails: " + nonExistingEmails);
        }

        // Add students to the classroom
        classroom.getStudentEmails().addAll(existingStudentEmails);
        classroomRepository.save(classroom);

    }

    @Override
    public Classroom findClassroomByCode(String code) {
        return classroomRepository.findByCode(code);
    }

    @Override
    public List<Classroom> getClassroomsByTeacherId(String teacherId) {
        return classroomRepository.findByTeacherId(teacherId);
    }

    @Override
    public Classroom archiveClassroom(String classroomId, String teacherId) {
        return null;
    }

    @Override
    public boolean archiveClassroom(Authentication authentication, String classroomId) {
        // Check if the authenticated user is a teacher
        UserEntity authenticatedUser = (UserEntity) authentication.getPrincipal();
        if (!authenticatedUser.isTeacher()) {
            throw new AccessDeniedException("Only teachers can archive classrooms");
        }

        // Fetch the classroom by id
        Optional<Classroom> optionalClassroom = classroomRepository.findById(classroomId);
        if (!optionalClassroom.isPresent()) {
            throw new ApplicationException("Classroom not found");
        }

        Classroom classroom = optionalClassroom.get();

        // Check if the authenticated teacher is the creator of the classroom
        if (!classroom.getCreatorId().equals(authenticatedUser.getId())) {
            throw new AccessDeniedException("You can only archive classrooms you have created");
        }

        // Archive the classroom
        classroom.setArchived(true);
        classroomRepository.save(classroom);

        return true;
    }

    @Override
    public List<Classroom> getAllClassrooms() {
        return classroomRepository.findAll();
    }

    @Override
    public Post createPost(String classroomId,  String userId,String content) {
        Optional<Classroom> classroomOptional = classroomRepository.findById(classroomId);
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        UserEntity user = userOptional.get();
        if (classroomOptional.isPresent()) {
            Classroom classroom = classroomOptional.get();
            Post post = new Post();
            post.setClassroomId(classroomId);

            post.setUserId(userId);
            post.setContent(content);
            post.setUsername(user.getUsername());
            post.setCreatedAt(new Date());
            post = postRepository.save(post);

            classroom.getPosts().add(post);
            classroomRepository.save(classroom);

            return post;
        } else {
            throw new IllegalArgumentException("Classroom not found.");
        }
    }



    @Override
    public List<Homework> getHomeworksForClassroom(String classroomId) {
        Optional<Classroom> classroomOptional = classroomRepository.findById(classroomId);
        if (classroomOptional.isPresent()) {
            return homeworkRepository.findByClassroomId(classroomId);
        } else {
            throw new IllegalArgumentException("Classroom not found.");
        }
    }

    @Override
    public Homework getHomeworkById(String homeworkId) {
        return homeworkRepository.findById(homeworkId)
                .orElseThrow(() -> new IllegalArgumentException("Homework not found."));
    }

    @Override
    public List<Comment> getCommentsForPost(String postId) {
        return commentRepository.findByPostId(postId);
    }

    @Override
    public Comment addCommentToPost(String postId, String content, String userId, String username) {
        Post post = postRepository.findById(postId).orElse(null);

        if (post != null) {
            // Create and populate a new Comment object
            Comment comment = new Comment();
            comment.setContent(content);
            comment.setPostId(postId);
            comment.setUserId(userId); // Set the userId
            comment.setUsername(username); // Set the username
            comment.setCreatedAt(new Date());

            // Add the comment to the post
            post.getComments().add(comment);

            // Save the post with the new comment
            postRepository.save(post);

            return comment;
        }

        return null; // Return null if post is not found
    }



    @Override
    public List<Classroom> searchClassroom(String keyword) {
        return classroomRepository.findByNameContainingIgnoreCase(keyword);
    }
    @Override
    public Classroom getClassroom(String classroomId) {
        Optional<Classroom> classroomOptional = classroomRepository.findById(classroomId);
        return classroomOptional.orElseThrow(() -> new IllegalArgumentException("Classroom not found"));
    }
    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    public boolean removeStudentFromClassroom(String classroomId, String email) {
        Optional<Classroom> classroomOpt = classroomRepository.findById(classroomId);

        if (classroomOpt.isPresent()) {
            Classroom classroom = classroomOpt.get();

            List<String> students = classroom.getStudentEmails();

            if (students.contains(email)) {
                students.remove(email);  // Remove the student from the list
                classroom.setStudentEmails(students);  // Update the classroom object
                classroomRepository.save(classroom);  // Save the updated classroom
                return true;
            }
        }
        return false;  // Classroom or student not found
    }
    public Classroom updateClassroomName(String id, String newName) {
        Optional<Classroom> optionalClassroom = classroomRepository.findById(id);

        if (optionalClassroom.isPresent()) {
            Classroom classroom = optionalClassroom.get();
            classroom.setName(newName);
            return classroomRepository.save(classroom); // Save the updated classroom
        } else {
            throw new RuntimeException("Classroom not found with id: " + id);
        }
    }
    public void deleteClassroom(String id) {
        Optional<Classroom> classroom = classroomRepository.findById(id);
        if (classroom.isPresent()) {
            classroomRepository.deleteById(id);
        } else {
            throw new RuntimeException("Classroom not found with id: " + id);
        }
    }
   public List<Comment> getCommentsByPostId(String postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        return postOptional.map(Post::getComments).orElse(Collections.emptyList());
    }
    public Classroom addExamToClassroom(String classroomId, Exams exam) {
        Optional<Classroom> optionalClassroom = classroomRepository.findById(classroomId);
        if (optionalClassroom.isPresent()) {
            Classroom classroom = optionalClassroom.get();
            classroom.getExams().add(exam); // Add the exam to the classroom's exam list
            return classroomRepository.save(classroom);
        }
        return null;
    }
    public List<Exams> getExamsByClassroomId(String classroomId) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found"));

        return classroom.getExams(); // Assuming Classroom entity has a List<Exams>
    }
    public List<Classroom> getJoinedClassrooms(String studentId) {
        // Fetch the student's email from the UserEntity repository (assuming you have that service)
        String studentEmail = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"))
                .getEmail();

        // Fetch classrooms where student is in the studentEmails list
        return classroomRepository.findByStudentEmailsContaining(studentEmail);
    }
    public Classroom findById(String classroomId) {
        // Find the classroom by its ID
        Optional<Classroom> classroom = classroomRepository.findById(classroomId);
        return classroom.orElse(null);  // Return the classroom or null if not found
    }
    public UserEntity findUserEntityByUsername(String email) {
        return userRepository.findUserByEmail(email);
    }




   }
