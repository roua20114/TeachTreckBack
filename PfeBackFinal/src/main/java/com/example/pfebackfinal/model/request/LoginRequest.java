package com.example.pfebackfinal.model.request;

import com.example.pfebackfinal.exception.ApplicationException;

/**
 * @author nidhal.ben-yarou
 */
public record LoginRequest(String username, String password) {
    public LoginRequest {
        if (username == null || username.isBlank()) {
            throw new ApplicationException("Username cannot be null or empty");
        }
        if (password == null || password.isBlank()) {
            throw new ApplicationException("Password cannot be null or empty");
        }
    }
}
