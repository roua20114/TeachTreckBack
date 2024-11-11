package com.example.pfebackfinal.presistence.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExamRequest {
    private String title;
    private String description;
    private String domaine;
    private String classroomId;
}
