package com.example.pfebackfinal.services;

import com.example.pfebackfinal.presistence.entity.*;

import java.util.List;

public interface IStudentService {

    Student getStudentProfile(String studentId);
    public Student updateStudentProfile(String studentId, StudentProfileUpdatedDTO updateDTO);
    public void deleteStudent(String studentId);
    public Student getStudent(String studentId);
}
