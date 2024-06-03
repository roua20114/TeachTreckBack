package com.example.pfebackfinal.exception;

import lombok.Getter;

/**
 * @author nidhal.ben-yarou
 */
@Getter
public class ApplicationException extends RuntimeException {
    private final String message;

    public ApplicationException(String message) {
        this.message = message;
    }
}
