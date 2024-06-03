package com.example.pfebackfinal.services;

import com.example.pfebackfinal.presistence.entity.Student;
import com.example.pfebackfinal.presistence.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository pupilRepository;

    public Student createPupil(String email, String name) {
        Student student = new Student();
        student.setEmail(email);
        student.setName(name);
        return pupilRepository.save(student);
    }
}

