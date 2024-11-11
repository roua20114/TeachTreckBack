package com.example.pfebackfinal.controllers;

import com.example.pfebackfinal.presistence.entity.*;
import com.example.pfebackfinal.presistence.repository.ClassroomRepository;
import com.example.pfebackfinal.presistence.repository.JoinRequestRepository;
import com.example.pfebackfinal.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor

@RequestMapping("/api/student")

@CrossOrigin(origins="http://localhost:4200")
public class StudentController {

    private final StudentService studentService;
    private final ExamsService examService;

    private final CourseService coursesService;
    private final ClassroomService classroomService;
    private final HomeworkSubmissionService homeworkSubmissionService;



    @GetMapping("/profile")
    public ResponseEntity<Student> getStudentProfile(@RequestParam String studentId) {
        try {
            Student student = studentService.getStudentProfile(studentId);
            return ResponseEntity.ok(student);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @PutMapping("/update_profile")
    public ResponseEntity<Student> updateStudentProfile(@RequestParam String studentId, @RequestBody StudentProfileUpdatedDTO updateDTO) {
        try {
            Student updatedStudent = studentService.updateStudentProfile(studentId, updateDTO);
            return ResponseEntity.ok(updatedStudent);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @DeleteMapping("deleteProfile/{studentId}")
    public ResponseEntity<String> deleteStudentProfile(@PathVariable String studentId) {
        try {
            studentService.deleteStudent(studentId);
            return ResponseEntity.ok("Student deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{courseId}/download")
    public ResponseEntity<InputStreamResource> downloadCourse(@PathVariable String courseId) throws IOException {
        try {
            Courses course = coursesService.getCourseById(courseId);

            // Assuming filePath contains the path to the downloadable file
            String filePath = course.getFilePath();

            // Get the file to be downloaded
            File file = new File(filePath);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            // Set content type and headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentLength(file.length());
            headers.setContentDispositionFormData("attachment", file.getName());

            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/search")
    public ResponseEntity<List<Courses>> searchCourses(@RequestParam String keyword) {
        List<Courses> courses = coursesService.searchCourses(keyword);
        return ResponseEntity.ok(courses);
    }
    @GetMapping("/searchRoom")
    public ResponseEntity<List<Classroom>> searchClassroom(@RequestParam String keyword) {
        List<Classroom> classroom = classroomService.searchClassroom(keyword);
        return ResponseEntity.ok(classroom);
    }


//    @PostMapping("/submit")
//    public ResponseEntity<String> submitHomework(
//            @RequestParam String studentId,
//            @RequestParam String classroomId,
//            @RequestParam String homeworkContent) {
//        try {
//            homeworkSubmissionService.submitHomework(studentId, classroomId, homeworkContent);
//            return ResponseEntity.ok("Homework submitted successfully.");
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
    @GetMapping("/submissions/{classroomId}")
    public ResponseEntity<List<HomeworkSubmission>> getHomeworkSubmissions(@PathVariable String classroomId) {
        try {
            List<HomeworkSubmission> submissions = homeworkSubmissionService.getHomeworkSubmissionsByClassroom(classroomId);
            return ResponseEntity.ok(submissions);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/approved/{studentId}")
    public ResponseEntity<List<Classroom>> getApprovedClassrooms(@PathVariable String studentId) {
        List<Classroom> classrooms = studentService.getApprovedClassrooms(studentId);
        return ResponseEntity.ok(classrooms);
    }
    @GetMapping("/joined/{studentId}")
    public ResponseEntity<List<Classroom>> getJoinedClassrooms(@PathVariable String studentId) {
        // Validate that the studentId is present and valid
        if (studentId == null || studentId.isEmpty()) {
            return ResponseEntity.badRequest().body(null);  // Return 400 Bad Request if studentId is invalid
        }

        try {
            List<Classroom> classrooms = classroomService.getJoinedClassrooms(studentId);
            return ResponseEntity.ok(classrooms);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // 500 if something goes wrong
        }
    }
    @GetMapping("/{classroomId}/studentlist")
    public ResponseEntity<List<String>> getStudentsInClassroom(@PathVariable String classroomId) {
        Classroom classroom = classroomService.getClassroom(classroomId);

        if (classroom != null) {
            List<String> students = classroom.getStudentEmails();
            return ResponseEntity.ok(students);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{classroomId}")
    public ResponseEntity<Classroom> getByIdClassroom(@PathVariable String classroomId) {
        Classroom classroom = classroomService.getClassroom(classroomId);

        if (classroom != null) {
            return ResponseEntity.ok(classroom);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/classroom/{classroomId}")
    public ResponseEntity<List<Courses>> getCoursesByClassroomId(@PathVariable String classroomId) {
        List<Courses> courses = coursesService.getCoursesByClassroom(classroomId);
        if (courses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(courses);
    }
//    @GetMapping("/exams/{classroomId}")
//    public ResponseEntity<List<Exams>> getExamsByClassroom(@PathVariable String classroomId) {
//        List<Exams> exams = examService.getExamsByClassroomId(classroomId);
//        System.out.println("Exams retrieved: " + exams.size());
//        if (exams.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        }
//        return ResponseEntity.ok(exams);
//    }
    @GetMapping("/allexams/{classroomId}")
    public ResponseEntity<List<Exams>> getExamsByClassroom(@PathVariable String classroomId) {
        List<Exams> exams = examService.getExamsByClassroomId(classroomId);
        return ResponseEntity.ok(exams);
    }
//    @PostMapping("/submit")
//    public ResponseEntity<String> submitAnswers(@RequestBody AnswerRequest answerRequest) {
//        examService.submitAnswers(answerRequest);
//        return ResponseEntity.ok("Answers submitted successfully.");
//    }


}
