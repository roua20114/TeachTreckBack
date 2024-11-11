package com.example.pfebackfinal.presistence.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StudentPerformanceDTO {
    private String studentId;
    private List<CourseDTO> enrolledCourses;
    private List<HomeworkSubmissionDTO> homeworkSubmissions;
    private List<PostInteractionDTO> postInteractions;
}
