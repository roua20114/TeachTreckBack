package com.example.pfebackfinal.presistence.entity;

import com.example.pfebackfinal.model.internal.RoleEnumeration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;

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
    private String profilePictureUrl;
    private byte[] profilePicture;

    private RoleEnumeration role;

    public boolean isTeacher() {
        return "TEACHER".equals(this.role);
    }


}
