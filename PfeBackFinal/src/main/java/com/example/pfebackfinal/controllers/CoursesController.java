package com.example.pfebackfinal.controllers;


import com.example.pfebackfinal.presistence.entity.Classroom;
import com.example.pfebackfinal.presistence.entity.Courses;
import com.example.pfebackfinal.presistence.entity.Teacher;
import com.example.pfebackfinal.services.ClassroomService;
import com.example.pfebackfinal.services.CourseService;
import com.example.pfebackfinal.services.ICourseService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import java.util.Optional;

@RestController
@RequestMapping("/courses")
@AllArgsConstructor
@PreAuthorize("hasAnyAuthority('TEACHER')")
@CrossOrigin(origins="http://localhost:4200")



public class CoursesController {
    private static final Logger logger = LoggerFactory.getLogger(CoursesController.class);

    private final ICourseService iCourseService;
    private final CourseService courseService;
    private final ClassroomService classroomService;

    private static final String UPLOAD_DIRECTORY = "C:/Users/lenovo/IdeaProjects/uploads/";

    @PostMapping("/add")
    public ResponseEntity<Courses> addCourseWithFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("courseType") String courseType,
            @RequestParam("classroomId") String classroomId,
            Principal principal) {
        try {
            // Fetch the authenticated teacher's username
            String username = principal.getName();
            Teacher teacher = iCourseService.findTeacherByUsername(username);
            Classroom classroom = classroomService.getClassroom(classroomId);

            // Ensure upload directory exists
            Path uploadPath = Paths.get(UPLOAD_DIRECTORY);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                logger.info("Created directory: {}", uploadPath.toAbsolutePath());
            }

            // Save the file
            String fileName = file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            file.transferTo(filePath.toFile());

            // Create and save the course
            Courses course = new Courses();
            course.setTitle(title);
            course.setDescription(description);
            course.setCourseType(courseType);
            course.setFilePath(fileName);  // Save only the file name
            course.setTeacher(teacher);
            course.setClassroom(classroom);

            Courses savedCourse = iCourseService.addCourse(course);
            return new ResponseEntity<>(savedCourse, HttpStatus.CREATED);
        } catch (IOException e) {
            logger.error("File transfer failed: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint to serve the uploaded files

    @PutMapping("/update/{courseId}")
    public ResponseEntity<Courses> updateCourse(
            @PathVariable("courseId") String courseId,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("courseType") String courseType,
            Principal principal) {
        try {
            // Fetch the authenticated teacher's username
            String username = principal.getName();
            Teacher teacher = iCourseService.findTeacherByUsername(username);

            // Find the course by ID
            Courses existingCourse = iCourseService.findCourseById(courseId);
            if (existingCourse == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Ensure the course is being updated by the original teacher
            if (!existingCourse.getTeacher().getUsername().equals(teacher.getUsername())) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);  // User is not authorized
            }

            // Update course details
            existingCourse.setTitle(title);
            existingCourse.setDescription(description);
            existingCourse.setCourseType(courseType);

            // If a new file is uploaded, save it
            if (file != null && !file.isEmpty()) {
                Path uploadPath = Paths.get(UPLOAD_DIRECTORY);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String fileName = file.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                file.transferTo(filePath.toFile());
                existingCourse.setFilePath(fileName);  // Update file path
            }

            // Save the updated course
            Courses updatedCourse = courseService.updateCourse(existingCourse);
            return new ResponseEntity<>(updatedCourse, HttpStatus.OK);

        } catch (IOException e) {
            logger.error("File transfer failed: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }







    @GetMapping("/my-courses")
    public ResponseEntity<List<Courses>> getMyCourses(@RequestParam("classroomId") String classroomId, Principal principal) {
        try {
            // Fetch the authenticated teacher's username
            String username = principal.getName();

            // Find the teacher by username
            Teacher teacher = iCourseService.findTeacherByUsername(username);

            // Fetch the courses for this teacher and the specified classroom
            List<Courses> courses = iCourseService.getCoursesByTeacherAndClassroom(teacher.getId(), classroomId);

            return new ResponseEntity<>(courses, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("An error occurred while fetching courses for the teacher: {}", e.getMessage(), e);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }







    @GetMapping("/{courseId}")
    public ResponseEntity<Courses> getCourseById(
            @PathVariable String courseId,
            Principal principal) {

        try {
            // Fetch the authenticated teacher's username
            String username = principal.getName();

            // Find the teacher by username
            Teacher teacher = iCourseService.findTeacherByUsername(username);

            // Fetch the course by its ID
            Courses course = iCourseService.getCourseById(courseId);

            // Check if the course belongs to the authenticated teacher
            if (!course.getTeacher().getId().equals(teacher.getId())) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN); // 403 Forbidden
            }

            return new ResponseEntity<>(course, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("An error occurred while fetching the course: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/delete/{courseId}")
    public ResponseEntity<Void> deleteCourse(
            @PathVariable String courseId,
            Principal principal) {

        try {
            // Fetch the authenticated teacher's username
            String username = principal.getName();

            // Find the teacher by username
            Teacher teacher = iCourseService.findTeacherByUsername(username);

            // Fetch the course by its ID
            Courses course = iCourseService.getCourseById(courseId);

            // Check if the course belongs to the authenticated teacher
            if (!course.getTeacher().getId().equals(teacher.getId())) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN); // 403 Forbidden
            }

            // Delete the course
            iCourseService.deleteCourse(courseId);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("An error occurred while deleting the course: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
