package com.example.pfebackfinal.presistence.entity;

import com.example.pfebackfinal.model.internal.RoleEnumeration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "users")
public class UserEntity {
    @Id
    private String id;
    private String email;
    private String username;
    private String firstName;
    private String lastName;

    private String mobile;
    private String gender;

    private String address;
    private String password;
    private String university;

    private RoleEnumeration role;
}
