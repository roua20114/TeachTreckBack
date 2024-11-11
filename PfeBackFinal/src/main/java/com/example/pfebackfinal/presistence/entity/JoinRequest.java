package com.example.pfebackfinal.presistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "join_requests")
public class JoinRequest {
    @Id
    private String id;



@DBRef
private Classroom  classroom;
@DBRef
private Student student;


    private String status;


}
