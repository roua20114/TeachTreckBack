package com.example.pfebackfinal.services;

import com.example.pfebackfinal.presistence.entity.Classroom;
import com.example.pfebackfinal.presistence.entity.Courses;
import com.example.pfebackfinal.presistence.entity.Teacher;
import com.example.pfebackfinal.presistence.repository.ClassroomRepository;
import com.example.pfebackfinal.presistence.repository.CoursesRepository;
import com.example.pfebackfinal.presistence.repository.TeacherRepository;
import com.example.pfebackfinal.presistence.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CourseService implements ICourseService{
    private final CoursesRepository coursesRepository;
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final ClassroomRepository classroomRepository;
    @Override
    public Courses addCourse(Courses course) {
        // Ensure the teacher reference is not null and valid
        if (course.getTeacher() != null && teacherRepository.findById(course.getTeacher().getId()).isPresent()) {
            return coursesRepository.save(course);
        } else {
            throw new RuntimeException("Teacher not found for the given course");
        }
    }


    @Override
    public List<Courses> getAllCourses() {
        return coursesRepository.findAll();
    }

    @Override
    public Courses getCourseById(String courseId) {
        return coursesRepository.findById(courseId).get();
    }


    public Courses updateCourse(Courses course) {
        // Ensure the course exists before saving
        if (course != null && course.getCourseId() != null) {
            // Save the updated course details in the database
            return coursesRepository.save(course);
        }
        return null;
    }
    @Override
    public void deleteCourse(String courseId) {
        coursesRepository.deleteById(courseId);

    }

    @Override
    public Teacher findTeacherByUsername(String email) {
        return teacherRepository.findByEmail(email);
    }

    @Override
    public List<Courses> getCoursesByTeacher(String teacherId) {
        return coursesRepository.findByTeacher_Id(teacherId);
    }

    @Override
    public List<Courses> searchCourses(String keyword) {
        return coursesRepository.findByTitleContainingIgnoreCase(keyword);
    }

    @Override
    public Courses findCourseById(String courseId) {
        Optional<Courses> optionalCourse = coursesRepository.findById(courseId);
        return optionalCourse.orElse(null);
    }

    @Override
    public List<Courses> getCoursesByTeacherAndClassroom(String teacherId, String classroomId) {
        return coursesRepository.findByTeacherIdAndClassroomId(teacherId, classroomId);
    }

//    public List<Courses> getCoursesByTeacherId() {
//        return coursesRepository.findByTeacherId();
//    }
//    public Optional<Courses> updateCoursebyTeacherId(String teacherId){
//        return coursesRepository.updateByTeacherId(teacherId);
//    }
public Courses updateCourseWithFile(String courseId, Courses updatedCourse, MultipartFile file) throws IOException {
    Optional<Courses> existingCourseOpt = coursesRepository.findById(courseId);

    if (existingCourseOpt.isPresent()) {
        Courses existingCourse = existingCourseOpt.get();
        existingCourse.setTitle(updatedCourse.getTitle());
        existingCourse.setDescription(updatedCourse.getDescription());
        existingCourse.setCourseType(updatedCourse.getCourseType());

        // If there's a new file, update the file path
        if (file != null && !file.isEmpty()) {
            String filePath = saveFile(file); // Custom method to save the file
            existingCourse.setFilePath(filePath);
        }

        return coursesRepository.save(existingCourse); // Save updated course
    } else {
        throw new RuntimeException("Course not found");
    }
}

    // Helper method to save the file
    private String saveFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get("uploads/", fileName);
        Files.createDirectories(filePath.getParent());
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return filePath.toString();
    }
    public List<Courses>getCoursesByClassroom( String classroomId){
        return coursesRepository.findByClassroomId(classroomId);

    }
}
