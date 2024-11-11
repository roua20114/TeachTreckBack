package com.example.pfebackfinal.services;

import com.example.pfebackfinal.presistence.entity.*;
import org.springframework.security.core.Authentication;

import java.util.Date;
import java.util.List;

public interface IClassroomService {
    Classroom createClassroom(Classroom classroom);
    Classroom addPupilToClassroom(String classroomId, String email);
    void addStudentsToClassroom(String classroomId, List<String> studentEmails);

    public Classroom findClassroomByCode(String code);
    List<Classroom>getClassroomsByTeacherId(String teacherId);
    Classroom archiveClassroom(String classroomId, String teacherId);
    Post createPost(String classroomId, String content, String userId);
    List<Homework> getHomeworksForClassroom(String classroomId);
    Homework getHomeworkById(String homeworkId);

    List<Comment> getCommentsForPost(String postId);
    Comment addCommentToPost(String postId, String content ,String userId, String username);
    List<Classroom> searchClassroom(String keyword);
    public Classroom getClassroom(String classroomId);
    public List<Student> getAllStudents();
    public boolean archiveClassroom(Authentication authentication, String classroomId);
    public List<Classroom>getAllClassrooms();

}

