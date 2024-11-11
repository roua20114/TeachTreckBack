package com.example.pfebackfinal.services;

import com.example.pfebackfinal.presistence.entity.HomeworkSubmission;

import java.util.List;

public interface IHomeworkSubmissionService {
    public void submitHomework(String studentId, String classroomId, String homeworkContent);
    public List<HomeworkSubmission> getHomeworkSubmissionsByClassroom(String classroomId);

}
