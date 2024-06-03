package com.example.pfebackfinal.model;

import com.example.pfebackfinal.exception.ApplicationException;
import com.example.pfebackfinal.model.internal.RoleEnumeration;
import org.springframework.util.StringUtils;

/**
 * @author nidhal.ben-yarou
 */
public record RegisterRequest(String username, String email, String password, String mobile, String address, String university, String gender,RoleEnumeration role, String studyField, String department,String domain) {
    public RegisterRequest {
        if (!StringUtils.hasText(username)) {
            throw new ApplicationException("Username is required");
        }
        if (!StringUtils.hasText(email)) {
            throw new ApplicationException("Email is required");
        }
        if (!StringUtils.hasText(password)) {
            throw new ApplicationException("Password is required");
        }


    }
}
