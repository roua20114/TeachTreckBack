package com.example.pfebackfinal.controllers;


import com.example.pfebackfinal.exception.ApplicationException;
import com.example.pfebackfinal.presistence.entity.*;
import com.example.pfebackfinal.presistence.repository.TeacherRepository;
import com.example.pfebackfinal.presistence.repository.UserRepository;
import com.example.pfebackfinal.services.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.*;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@RestController
@RequestMapping("/teacher-actions")
@PreAuthorize("hasAnyAuthority('TEACHER')")

@AllArgsConstructor
@CrossOrigin(origins="http://localhost:4200")
public class TeacherController {
    private final ITeacherService iTeacherService;
    private final UserRepository userRepository;
    private final ICourseService iCourseService;
    private final ClassroomService classroomService;
    private final TeacherService teacherService;
    private final TeacherRepository teacherRepository;
    private final UserService userService;

//    @Value("${profile.upload-dir}")
//    private String uploadDir;

    @GetMapping("/profileTeacher")

    public Teacher getProfile(@RequestParam String email) {
        return iTeacherService.getProfileByEmail(email)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
    }
    @GetMapping("/api/teacher/{id}")
    public ResponseEntity<UserEntity> getTeacherProfile(@PathVariable String id) {
        UserEntity teacher = userService.findById(id);
        if (teacher != null) {
            if (teacher.getProfilePictureUrl() != null) {
                String fullUrl = "http://localhost:8081/api/profile/pictures/" + teacher.getProfilePictureUrl();
                teacher.setProfilePictureUrl(fullUrl);
            }
            return ResponseEntity.ok(teacher);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/update-teacher-profile")
    public Teacher updateProfile(@RequestParam String email, @RequestBody Teacher updatedTeacher) {
        return iTeacherService.updateProfile(email, updatedTeacher);
    }
    @GetMapping("/profile-picture/{id}")
    public ResponseEntity<byte[]> getTeacherProfilePicture(@PathVariable String id) {
        UserEntity teacher = userService.findById(id);
        if (teacher != null && teacher.getProfilePicture() != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // Adjust based on your image type
                    .body(teacher.getProfilePicture());
        } else {
            return ResponseEntity.notFound().build();
        }
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

    @GetMapping("/search")
    public ResponseEntity<List<Courses>> searchCourses(@RequestParam String keyword) {
        List<Courses> courses = iCourseService.searchCourses(keyword);
        return ResponseEntity.ok(courses);
    }
    @GetMapping("/classroom/search")
    public ResponseEntity<List<Classroom>> searchClassroom(@RequestParam String keyword) {
        List<Classroom> classroom = classroomService.searchClassroom(keyword);
        return ResponseEntity.ok(classroom);
    }
    @PostMapping("/upload")
    public ResponseEntity<String> uploadProfilePicture(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file uploaded");
        }

        try {
            // Save file in the specified directory
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get("C:/Users/lenovo/IdeaProjects/uploads/profile-pictures", fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Return the URL to access the profile picture
            String fileDownloadUri = "http://localhost:8081/api/profile/pictures/" + fileName;
            return ResponseEntity.ok(fileDownloadUri);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
        }
    }

    @GetMapping("/pictures/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> getProfilePicture(@PathVariable String filename) {
        try {
            Path file = Paths.get("C:/Users/lenovo/IdeaProjects/role-based-auth/PfeBackFinal/src/main/resources/uploads/profile-pictures").resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok().body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/{teacherId}/uploadProfilePicture")
    public ResponseEntity<String> uploadProfilePicture(
            @PathVariable String teacherId,
            @RequestParam("file") MultipartFile file) {
        try {
            // Pass the file and teacherId to the service layer
            String profilePictureUrl = teacherService.uploadProfilePicture(teacherId, file);
            return ResponseEntity.ok(profilePictureUrl); // Return the URL of the uploaded picture
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error uploading profile picture: " + e.getMessage());
        }
    }
    @PutMapping("/updateProfile")
    public ResponseEntity<?> updateProfile(@RequestParam(value = "file", required = false) MultipartFile file,
                                           @RequestParam("email") String email,
                                           @RequestParam("username") String username,
                                           @RequestParam("firstName") String firstName,
                                           @RequestParam("lastName") String lastName,
                                           @RequestParam("mobile") String mobile,
                                           @RequestParam("address") String address) {
        try {
            // Validate input parameters
            if (email == null || username == null || firstName == null || lastName == null || mobile == null || address == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("All fields must be provided.");
            }

            // Handle the profile picture update
            if (file != null && !file.isEmpty()) {
                userService.deleteCurrentProfilePicture();
                String profilePictureUrl = userService.saveProfilePictur(file);
                userService.updateProfilePictureUrl(profilePictureUrl);
                System.out.println("File name: " + file.getOriginalFilename());
                System.out.println("File size: " + file.getSize());
            } else {
                // Log the case when no file is provided, if needed
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty or not provided.");


            }

            // Update the other profile credentials
            userService.updateProfile(email, username, firstName, lastName, mobile, address);

            return ResponseEntity.ok("Profile updated successfully!");
        } catch (Exception e) {
            // Log the exception for debugging

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update the profile: " + e.getMessage());
        }
    }

}






