package com.example.pfebackfinal.services;

import com.example.pfebackfinal.presistence.entity.Classroom;
import com.example.pfebackfinal.presistence.repository.ClassroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClassroomService {

    private final ClassroomRepository classroomRepository;

    public Classroom createClassroom(String name) {
        Classroom classroom = new Classroom();
        classroom.setName(name);
        classroom.setCode(UUID.randomUUID().toString());
        return classroomRepository.save(classroom);
    }

    public Classroom addPupilToClassroom(String code, String email) {
        Classroom classroom = classroomRepository.findByCode(code);
        if (classroom != null) {
            classroom.getStudentEmails().add(email);
            return classroomRepository.save(classroom);
        }
        return null;
    }
}
