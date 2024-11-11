package com.example.pfebackfinal.model.request;

import com.example.pfebackfinal.exception.ApplicationException;

/**
 * @author nidhal.ben-yarou
 */
public record LoginRequest(String email, String password) {
    public LoginRequest {
        if (email == null || email.isBlank()) {
            throw new ApplicationException("email cannot be null or empty");
        }
        if (password == null || password.isBlank()) {
            throw new ApplicationException("Password cannot be null or empty");
        }
    }
}
