package com.example.pfebackfinal.controllers;


import com.example.pfebackfinal.presistence.entity.Courses;
import com.example.pfebackfinal.presistence.entity.Teacher;
import com.example.pfebackfinal.services.CourseService;
import com.example.pfebackfinal.services.ICourseService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
@AllArgsConstructor
@PreAuthorize("hasAnyAuthority('TEACHER')")
@CrossOrigin

public class CoursesController {
    private final ICourseService iCourseService;
    private final CourseService courseService;

    @PostMapping("/add")
    public ResponseEntity<Courses> addCourse(@RequestBody Courses course, Principal principal) {
        String username = principal.getName();
        Teacher teacher = iCourseService.findTeacherByUsername(username);
        if (teacher!= null) {
            Courses savedCourse = iCourseService.addCourse(course, teacher.getId());
            return new ResponseEntity<>(savedCourse, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
    @GetMapping("/my-courses")
    public ResponseEntity<List<Courses>> getMyCourses(@RequestBody Courses courses, Principal principal) {
        String username = principal.getName();
        Teacher teacher = iCourseService.findTeacherByUsername(username);
        if (teacher!= null) {
            List<Courses> cours= courseService.getCoursesByTeacherId(teacher.getId());
            return new ResponseEntity<>(cours,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
//    @PutMapping("/updateCourses")
//    public ResponseEntity<Courses> updateCourse(@RequestBody Courses courses, Principal principal){
//        String username = principal.getName();
//        Teacher teacher = iCourseService.findTeacherByUsername(username);
//        if (teacher!= null) {
//        Optional<Courses> cours= courseService.updateCoursebyTeacherId(teacher.getId());
//            return new ResponseEntity<>(HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }
//
//    }



    @GetMapping("/{courseId}")
    public ResponseEntity<Courses> getCourseById(@PathVariable("courseId") String courseId){
        Courses courses= iCourseService.getCourseById(courseId);
        if (courses==null){
            return ResponseEntity.notFound().build();
        }else {
            return ResponseEntity.ok(courses);
        }


    }

    @DeleteMapping("/deleteCourse/{courseId}")
    public void deleteCourse(@PathVariable String courseId){
        iCourseService.deleteCourse(courseId);
    }


}
