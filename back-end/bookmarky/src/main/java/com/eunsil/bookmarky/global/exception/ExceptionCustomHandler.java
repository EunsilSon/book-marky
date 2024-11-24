package com.eunsil.bookmarky.global.exception;

import com.eunsil.bookmarky.global.response.ApiResponse;
import com.eunsil.bookmarky.global.response.ResponseUtil;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class ExceptionCustomHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<String> handleValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException : {}", e.getMessage());
        return ResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST, e.getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(DuplicateRequestException.class)
    public ApiResponse<String> handleDuplicateException(DuplicateRequestException e) {
        log.error("DuplicateRequestException : {}", e.getMessage());
        return ResponseUtil.createErrorResponse(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ApiResponse<String> handleElementException(NoSuchElementException e) {
        log.error("NoSuchElementException : {}", e.getMessage());
        return ResponseUtil.createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ApiResponse<String> handleUserException(UsernameNotFoundException e) {
        log.error("UsernameNotFoundException : {}", e.getMessage());
        return ResponseUtil.createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(CustomParsingException.class)
    public ApiResponse<String> handleParsingException(CustomParsingException e) {
        log.error("CustomParsingException : {}", e.getMessage());
        return ResponseUtil.createErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<String> handleUncaughtException(Exception e) {
        log.error("Unexpected error occurred : {}", e.getMessage());
        String UNCAUGHT_EXCEPTION_MESSAGE = "An unexpected Server error occurred. Please try again later.";
        return ResponseUtil.createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, UNCAUGHT_EXCEPTION_MESSAGE);
    }
}
