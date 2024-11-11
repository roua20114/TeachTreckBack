package com.example.pfebackfinal.presistence.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentProfileUpdatedDTO {
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String mobile;
    private String gender;
    private String address;
    private String university;
    private String studyField;
    private String name;
}
