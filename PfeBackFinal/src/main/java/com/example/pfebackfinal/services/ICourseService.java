package com.example.pfebackfinal.services;

import com.example.pfebackfinal.presistence.entity.Courses;
import com.example.pfebackfinal.presistence.entity.Teacher;

import java.util.List;

public interface ICourseService {
    public Courses addCourse(Courses courses, String teacherId);

    public List<Courses> getAllCourses();
    public Courses getCourseById(String courseId);
    public Courses updateCourse(Courses courses);
    public void deleteCourse(String courseId);
    Teacher findTeacherByUsername(String username);


}
