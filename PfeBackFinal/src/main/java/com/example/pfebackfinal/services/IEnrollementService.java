package com.example.pfebackfinal.services;

public interface IEnrollementService {
    public void enrollStudentInCourse(String studentId, String courseId);
    public void unenrollStudentFromCourse(String studentId, String courseId);
    public void addCommentToCourse(String studentId, String courseId, String commentText);
    public void addCommentToPost(String studentId, String postId, String content) ;

}
