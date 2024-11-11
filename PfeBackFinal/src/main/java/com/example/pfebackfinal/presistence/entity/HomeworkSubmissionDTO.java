package com.example.pfebackfinal.presistence.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Setter
@Getter
public class HomeworkSubmissionDTO {
    private String homeworkId;
    private String homeworkTitle;
    private String submissionStatus;
    private Date submissionDate;
}
