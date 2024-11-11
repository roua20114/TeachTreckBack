package com.example.pfebackfinal.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException() {
        ErrorResponse errorResponse = new ErrorResponse("the provided email is not found");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles RuntimeException and returns an error response with status code INTERNAL_SERVER_ERROR.
     *
     * @param ex The RuntimeException that was thrown.
     * @return ResponseEntity with HTTP status indicating the error and a corresponding error response.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        if(ex instanceof HttpMessageNotReadableException httpMessageNotReadableException) {
            if(httpMessageNotReadableException.getRootCause() instanceof ApplicationException applicationException){
                return handleApplicationException(applicationException);
            }
        }
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles AccessDeniedException and returns an error response with status code UNAUTHORIZED.
     *
     * @param ex The AccessDeniedException that was thrown.
     * @return ResponseEntity with HTTP status indicating the error and a corresponding error response.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles ApplicationException and returns an error response with status code BAD_REQUEST.
     *
     * @param ex The ApplicationException that was thrown.
     * @return ResponseEntity with HTTP status indicating the error and a corresponding error response.
     */
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Represents an error response containing an error code and a message.
     */
    public record ErrorResponse(String message) {
        public ErrorResponse {
            log.debug("message {}", message);
        }
    }
}
