package com.example.pfebackfinal.services;

import com.example.pfebackfinal.presistence.entity.Teacher;
import com.example.pfebackfinal.presistence.repository.TeacherRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class TeacherService implements ITeacherService{
    private final TeacherRepository teacherRepository;
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
}
