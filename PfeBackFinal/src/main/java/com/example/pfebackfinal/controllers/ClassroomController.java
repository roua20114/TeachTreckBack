package com.example.pfebackfinal.controllers;

import com.example.pfebackfinal.exception.ApplicationException;
import com.example.pfebackfinal.model.internal.RoleEnumeration;
import com.example.pfebackfinal.presistence.entity.*;
import com.example.pfebackfinal.presistence.repository.UserRepository;
import com.example.pfebackfinal.services.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequiredArgsConstructor

@RequestMapping("/private/classroom")

@PreAuthorize("hasAnyAuthority('TEACHER')")
@CrossOrigin(origins="http://localhost:4200")



public class ClassroomController {

    private final ClassroomService classroomService;
    private final UserRepository userRepository;
    private final ICourseService iCourseService;
    private final TeacherService teacherService;
    private final CourseService courseService;
    private final JoinRequestService joinRequestService;
    private static final Logger logger = LoggerFactory.getLogger(ClassroomController.class);


    @PostMapping("/addClass")
    public ResponseEntity<Classroom> createClassroom(@RequestBody Classroom classroom, Principal principal) {
        // Get the authenticated teacher's email from the Principal object
        String username = principal.getName();
        Teacher teacher = iCourseService.findTeacherByUsername(username);

        if (teacher == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        classroom.setTeacher(teacher);

        try {
            Classroom createdClassroom = classroomService.createClassroom(classroom);
            return new ResponseEntity<>(createdClassroom, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/{classroomId}/studentss")
    public ResponseEntity<String> addPupilToClassroom(@PathVariable String classroomId, @RequestParam String email) {
        Classroom classroom = classroomService.addPupilToClassroom(classroomId, email);

        if (classroom != null) {
            return ResponseEntity.ok("Pupil added successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add pupil.");
        }
    }

    @GetMapping("/{classroomId}/allstudents")
    public ResponseEntity<List<String>> getStudentsInClassroom(@PathVariable String classroomId) {
        Classroom classroom = classroomService.getClassroom(classroomId);

        if (classroom != null) {
            List<String> students = joinRequestService.getAllStudentEmailsInClassroom(classroomId);

            return ResponseEntity.ok(students);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/myclassrooms")
    public ResponseEntity<List<Classroom>> getMyClassrooms(Principal principal) {
        Optional<UserEntity> userOptional = userRepository.findByEmail(principal.getName());
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            if (user.getRole() == RoleEnumeration.TEACHER) {
                List<Classroom> classrooms = classroomService.getClassroomsByTeacherId(user.getId());
                return ResponseEntity.ok(classrooms);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }
    }
    @PostMapping("/{classroomId}/archive")
    public ResponseEntity<String> archiveClassroom(@PathVariable String classroomId, Authentication authentication) {
        try {
            boolean archived = classroomService.archiveClassroom(authentication, classroomId);
            if (archived) {
                return new ResponseEntity<>("Classroom archived successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Classroom could not be archived", HttpStatus.BAD_REQUEST);
            }
        } catch (ApplicationException e) {
            return new ResponseEntity<>("Classroom not found", HttpStatus.NOT_FOUND);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>("Access denied", HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
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
    @DeleteMapping("/{classroomId}/students")
    public ResponseEntity<String> removeStudentFromClassroom(
            @PathVariable String classroomId,
            @RequestParam String email) {

        boolean isRemoved = classroomService.removeStudentFromClassroom(classroomId, email);

        if (isRemoved) {
            return ResponseEntity.ok("Student removed successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Student or classroom not found.");
        }
    }

    @PutMapping("/{id}/edit-name")
    public ResponseEntity<Classroom> editClassroomName(@PathVariable String id, @RequestBody Map<String, String> request) {
        String newName = request.get("name");
        if (newName == null || newName.isEmpty()) {
            return ResponseEntity.badRequest().body(null); // Validation
        }

        Classroom updatedClassroom = classroomService.updateClassroomName(id, newName);
        return ResponseEntity.ok(updatedClassroom);
    }
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteClassroom(@PathVariable String id) {
        classroomService.deleteClassroom(id);
        return ResponseEntity.noContent().build();  // Return 204 No Content on successful deletion
    }
    @GetMapping("/{classroomId}/exams")
    public ResponseEntity<?> getExamsByClassroomId(@PathVariable String classroomId) {
        try {
            List<Exams> exams = classroomService.getExamsByClassroomId(classroomId);
            if (exams != null && !exams.isEmpty()) {
                return ResponseEntity.ok(exams);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No exams found for this classroom");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching exams");
        }
    }

}
