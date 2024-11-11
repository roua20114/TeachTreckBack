package com.example.pfebackfinal.services;

import com.example.pfebackfinal.presistence.entity.Teacher;
import com.example.pfebackfinal.presistence.repository.TeacherRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TeacherService implements ITeacherService{
    private final TeacherRepository teacherRepository;
    private static final String UPLOAD_DIR = "C:/Users/lenovo/IdeaProjects/role-based-auth/PfeBackFinal/src/main/resources/folders/";
    @Override
    public Optional<Teacher> getProfileByEmail(String email) {
        return Optional.ofNullable(teacherRepository.findByEmail(email));
    }

    @Override
    public Teacher updateProfile(String email, Teacher updatedTeacher) {
        Teacher teacher = teacherRepository.findByEmail(email);
        if (teacher == null) {
            throw new RuntimeException("Teacher not found");
    }
        teacher.setFirstName(updatedTeacher.getFirstName());
        teacher.setLastName(updatedTeacher.getLastName());
        teacher.setUsername(updatedTeacher.getUsername());
        teacher.setMobile(updatedTeacher.getMobile());
        teacher.setGender(updatedTeacher.getGender());
        teacher.setAddress(updatedTeacher.getAddress());
        teacher.setPassword(updatedTeacher.getPassword());
        teacher.setUniversity(updatedTeacher.getUniversity());
        teacher.setDepartment(updatedTeacher.getDepartment());
        teacher.setDomain(updatedTeacher.getDomain());


        return teacherRepository.save(teacher);
    }

    @Override
    public void deleteProfile(String email) {

        Teacher teacher = teacherRepository.findByEmail(email);
        if (teacher == null) {
            throw new RuntimeException("Teacher not found");
        }


        teacherRepository.delete(teacher);
    }

    @Override
    public Teacher findByEmail(String email) {
        return teacherRepository.findByEmail(email);
    }

    @Override
    public Teacher findTeacherByUsername(String username) {
        return teacherRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Teacher not found with username: " + username));
    }
    public String uploadProfilePicture(String teacherId, MultipartFile file) throws IOException {
        // Find the teacher by ID
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found with id: " + teacherId));

        // Generate a file path
        String fileName = teacherId + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR + fileName);

        // Save the file locally
        Files.write(filePath, file.getBytes());

        // Generate a URL for the profile picture (this could be a public URL)
        String profilePictureUrl = "/uploads/" + fileName;

        // Update the teacher's profile with the picture URL
        teacher.setProfilePictureUrl(profilePictureUrl);
        teacherRepository.save(teacher);

        return profilePictureUrl; // Return the URL of the uploaded file
    }
}
