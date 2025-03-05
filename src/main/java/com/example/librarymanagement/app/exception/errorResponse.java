package com.example.librarymanagement.app.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class errorResponse extends RuntimeException {
    public errorResponse(String message) {
        super(message);
    }
}
