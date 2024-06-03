package com.example.pfebackfinal.presistence.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "enrollements")

public class Enrollement {
    @Id
    private String enrollementId;
    private String courseName;
    private String enrolledDate;
    private String enrolledUsername;
    private String instructorName;
    private String instructorInstitution;
    private String enrolledCount;
    private String skilllevel;
    private String description;
}
