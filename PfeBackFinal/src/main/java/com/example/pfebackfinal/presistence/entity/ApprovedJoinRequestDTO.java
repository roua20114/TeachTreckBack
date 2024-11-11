package com.example.pfebackfinal.presistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApprovedJoinRequestDTO {
    private String classroomId;
    private String classroomName;
    private String studentEmail;
    private String joinRequestStatus;
}
