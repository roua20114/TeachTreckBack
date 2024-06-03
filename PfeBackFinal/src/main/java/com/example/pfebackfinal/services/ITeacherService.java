package com.example.pfebackfinal.services;

import com.example.pfebackfinal.presistence.entity.Teacher;

import java.util.Optional;

public interface ITeacherService {
    Optional<Teacher> getProfileByEmail(String email);
    Teacher updateProfile(String email, Teacher updatedTeacher);
    void deleteProfile(String email);
}
