package com.example.librarymanagement.app.exception;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


   private Map<String, Object> getErrorAttributes(Exception ex, WebRequest request, HttpStatus status) {
        Map<String, Object> errorAttributes = new LinkedHashMap<>();
        errorAttributes.put("timestamp", System.currentTimeMillis());
        errorAttributes.put("status", status.value());
        errorAttributes.put("error", status.getReasonPhrase());
        errorAttributes.put("message", ex.getMessage());
        errorAttributes.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());

        logger.error("Exception: {}", ex.getMessage(), ex);
        return errorAttributes;
    }

    @ExceptionHandler(errorResponse.class)
    public ResponseEntity<Object> handleErrorResponse(errorResponse ex, WebRequest request) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(getErrorAttributes(ex, request, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllUncaughtException(Exception ex, WebRequest request) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(getErrorAttributes(ex, request, HttpStatus.INTERNAL_SERVER_ERROR));
    }

}