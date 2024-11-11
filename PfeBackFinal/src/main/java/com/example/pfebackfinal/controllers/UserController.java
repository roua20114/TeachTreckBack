package com.example.pfebackfinal.controllers;


import com.example.pfebackfinal.presistence.entity.Classroom;
import com.example.pfebackfinal.presistence.entity.UserEntity;
import com.example.pfebackfinal.services.ClassroomService;
import com.example.pfebackfinal.services.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user-actions")

@AllArgsConstructor
@CrossOrigin(origins="http://localhost:4200")

public class UserController {
    private final ClassroomService classroomService;
    private final UserService userService;
    @GetMapping("/allclassrooms")
    public ResponseEntity<List<Classroom>> getAllClassrooms() {
        List<Classroom> classrooms = classroomService.getAllClassrooms();
        return ResponseEntity.ok(classrooms);
    }
    @PutMapping("/updateprofile/{id}")
    public ResponseEntity<UserEntity> updateUserProfile(
            @PathVariable String id,
            @RequestParam(value = "profilePicture", required = false) MultipartFile file,
            @RequestPart("updatedUser") UserEntity updatedUser) {
        try {
            // Handle file upload logic
            if (file != null && !file.isEmpty()) {
                updatedUser.setProfilePicture(file.getBytes());  // Store the image as byte array
                updatedUser.setProfilePictureUrl("/user-actions/" + id + "/profilePicture");  // URL to retrieve the picture
            }

            // Update user profile
            UserEntity user = userService.updateUserProfile(id, updatedUser);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            e.printStackTrace(); // Log the error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

//    private String saveFile(MultipartFile file) throws IOException {
//        String uploadDir = "uploads/"; // Specify your upload directory
//        Path uploadPath = Paths.get(uploadDir);
//        if (!Files.exists(uploadPath)) {
//            Files.createDirectories(uploadPath); // This ensures the directory is created if not present
//        }
//        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
//        Path filePath = uploadPath.resolve(fileName);
//
//        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//        return "/uploads/" + fileName; // Or return a URL to access the file
//    }
    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable String id) {
        UserEntity user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/profile/{id}")
    public ResponseEntity<UserEntity> getUserProfile(@PathVariable String id) {
        Optional<UserEntity> user = userService.getUserrById(id); // Fetch the user by ID

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Return 404 if the user is not found
        }
    }
    @DeleteMapping("/profiledelete/{id}")
    public ResponseEntity<String> deleteUserProfile(@PathVariable String id) {
        try {
            boolean isDeleted = userService.deleteUserById(id);
            if (isDeleted) {
                return ResponseEntity.ok("User deleted successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user.");
        }
    }
    @PostMapping("/{id}/uploadProfilePicture")
    public ResponseEntity<String> uploadProfilePicture(@PathVariable String id, @RequestParam("file") MultipartFile file) {
        try {
            userService.saveProfilePicture(id, file);
            return ResponseEntity.ok("Profile picture uploaded successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload profile picture.");
        }
    }

    // Retrieve Profile Picture
    @GetMapping("/{id}/profilePicture")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable String id) {
        byte[] image = userService.getProfilePicture(id);
        if (image != null) {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
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



}
