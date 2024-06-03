package com.example.pfebackfinal.controllers;


import com.example.pfebackfinal.presistence.entity.Courses;
import com.example.pfebackfinal.presistence.entity.Student;
import com.example.pfebackfinal.presistence.entity.Teacher;
import com.example.pfebackfinal.presistence.entity.UserEntity;
import com.example.pfebackfinal.presistence.repository.UserRepository;
import com.example.pfebackfinal.services.ICourseService;
import com.example.pfebackfinal.services.ITeacherService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher-actions")
@PreAuthorize("hasAuthority('TEACHER')")
@CrossOrigin
@AllArgsConstructor
public class TeacherController {
    private final ITeacherService iTeacherService;
    private final UserRepository userRepository;
    private final ICourseService iCourseService;


    @GetMapping("/profileTeacher")

    public Teacher getProfile(@RequestParam String email) {
        return iTeacherService.getProfileByEmail(email)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
    }
    @PutMapping("/update-teacher-profile")
    public Teacher updateProfile(@RequestParam String email, @RequestBody Teacher updatedTeacher) {
        return iTeacherService.updateProfile(email, updatedTeacher);
    }
    @DeleteMapping("/delete-teacher-profile")
    public String deleteProfile(@RequestParam String email) {
        iTeacherService.deleteProfile(email);
        return "Teacher profile deleted successfully";
    }
    @GetMapping("/students")
    public ResponseEntity<List<UserEntity>> users() {
        List<UserEntity> users = userRepository.findAll();
        users = users.stream().filter(Student.class::isInstance).toList();
        return ResponseEntity.ok(users);
    }
//    @PostMapping("/addCourse")
//    public ResponseEntity<Courses>saveCourses(@RequestBody Courses courses){
//        Courses course= iCourseService.addCourse(courses);
//        return ResponseEntity.ok(course) ;
//    }
    @GetMapping("/coursesList")
    public ResponseEntity<List<Courses>> getAllCourse(){
        return ResponseEntity.ok(iCourseService.getAllCourses());
    }
    @GetMapping("/{courseId}")
    public ResponseEntity<Courses> getCourseById(@PathVariable("courseId") String courseId) {
        Courses courses = iCourseService.getCourseById(courseId);
        if (courses == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(courses);
        }
    }
    @PutMapping("/updateCourses")
    public ResponseEntity<Courses> updateCourse(@RequestBody Courses courses){
        Courses course= iCourseService.updateCourse(courses);
        return ResponseEntity.ok(course);
    }
    @DeleteMapping("/deleteCourse/{courseId}")
    public String deleteCourse(@PathVariable String courseId){
        iCourseService.deleteCourse(courseId);
        return "Course deleted Successfully";
    }




}
