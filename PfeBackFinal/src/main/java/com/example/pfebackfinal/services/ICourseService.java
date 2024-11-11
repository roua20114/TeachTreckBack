package com.example.pfebackfinal.services;

import com.example.pfebackfinal.presistence.entity.Classroom;
import com.example.pfebackfinal.presistence.entity.Courses;
import com.example.pfebackfinal.presistence.entity.Teacher;

import java.util.List;

public interface ICourseService {
    public Courses addCourse(Courses courses);

    public List<Courses> getAllCourses();
    public Courses getCourseById(String courseId);

    public void deleteCourse(String courseId);
    Teacher findTeacherByUsername(String email);
    List<Courses> getCoursesByTeacher(String teacherId);
    List<Courses> searchCourses(String keyword);
    public Courses findCourseById(String courseId);


    List<Courses> getCoursesByTeacherAndClassroom(String teacherId, String classroomId);
}
