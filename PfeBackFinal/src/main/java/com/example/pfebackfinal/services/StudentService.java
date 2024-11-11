package com.example.pfebackfinal.services;

import com.example.pfebackfinal.presistence.entity.*;
import com.example.pfebackfinal.presistence.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService implements IStudentService{

    private final StudentRepository pupilRepository;
    private final UserRepository userRepository;
    private final ClassroomRepository classroomRepository;
    private final HomeworkRepository homeworkRepository;
    private final PostRepository postRepository;
    private final JoinRequestRepository joinRequestRepository;

    public Student findById(String studentId) {
        Optional<Student> student = pupilRepository.findById(studentId);

        if (student.isPresent()) {
            return student.get();
        } else {
            throw new RuntimeException("Student not found with ID: " + studentId);
        }
    }



    @Override
    public Student getStudentProfile(String studentId) {
        Optional<Student> studentOpt = pupilRepository.findById(studentId);
        if (studentOpt.isPresent()) {
            return studentOpt.get();
        } else {
            throw new IllegalArgumentException("Student not found");
        }
    }

    @Override
    public Student updateStudentProfile(String studentId, StudentProfileUpdatedDTO updateDTO) {
        Optional<Student> studentOpt = pupilRepository.findById(studentId);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();

            student.setEmail(updateDTO.getEmail());
            student.setUsername(updateDTO.getUsername());
            student.setFirstName(updateDTO.getFirstName());
            student.setLastName(updateDTO.getLastName());
            student.setMobile(updateDTO.getMobile());
            student.setGender(updateDTO.getGender());
            student.setAddress(updateDTO.getAddress());
            student.setUniversity(updateDTO.getUniversity());
            student.setStudyField(updateDTO.getStudyField());
            student.setName(updateDTO.getName());

            return pupilRepository.save(student);
        } else {
            throw new IllegalArgumentException("Student not found");
        }
    }

    @Override
    public void deleteStudent(String studentId) {
        Optional<Student> studentOpt = pupilRepository.findById(studentId);
        if (studentOpt.isPresent()) {
            pupilRepository.deleteById(studentId);
        } else {
            throw new IllegalArgumentException("Student not found");
        }
    }
    @Override
    public Student getStudent(String studentId) {
        Optional<Student> studentOptional = pupilRepository.findById(studentId);
        return studentOptional.orElseThrow(() -> new IllegalArgumentException("Student not found"));
    }
    public List<Classroom> getApprovedClassrooms(String studentId) {
        List<JoinRequest> approvedRequests = joinRequestRepository.findByStudentIdAndStatusTrue(studentId);
        return approvedRequests.stream()
                .map(request -> request.getClassroom())
                .collect(Collectors.toList());
    }


}

