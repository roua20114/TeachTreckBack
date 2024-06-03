package com.example.pfebackfinal.services;

import com.example.pfebackfinal.presistence.entity.Courses;
import com.example.pfebackfinal.presistence.entity.Teacher;
import com.example.pfebackfinal.presistence.repository.CoursesRepository;
import com.example.pfebackfinal.presistence.repository.TeacherRepository;
import com.example.pfebackfinal.presistence.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CourseService implements ICourseService{
    private final CoursesRepository coursesRepository;
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    @Override
    public Courses addCourse(Courses course, String teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(() -> new RuntimeException("Teacher not found"));
        course.setTeacher(teacher);
        return coursesRepository.save(course);
    }



    @Override
    public List<Courses> getAllCourses() {
        return coursesRepository.findAll();
    }

    @Override
    public Courses getCourseById(String courseId) {
        return coursesRepository.findById(courseId).get();
    }

    @Override
    public Courses updateCourse(Courses courses) {
        return coursesRepository.save(courses);
    }

    @Override
    public void deleteCourse(String courseId) {
        coursesRepository.deleteById(courseId);

    }

    @Override
    public Teacher findTeacherByUsername(String username) {
        return teacherRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Teacher not found with username: " + username));
    }

    public List<Courses> getCoursesByTeacherId(String teacherId) {
        return coursesRepository.findByTeacherId(teacherId);
    }
//    public Optional<Courses> updateCoursebyTeacherId(String teacherId){
//        return coursesRepository.updateByTeacherId(teacherId);
//    }
}
