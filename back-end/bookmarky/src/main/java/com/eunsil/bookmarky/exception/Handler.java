package com.eunsil.bookmarky.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class Handler {

    private final ResponseService responseService;

    public Handler(ResponseService responseService) {
        this.responseService = responseService;
    }

    /**
     * VO 유효성 검증 실패
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return responseService.getErrorResponse(e.getStatusCode(), e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    /**
     * 트랜잭션 롤백
     */
    @ExceptionHandler(UnexpectedRollbackException.class)
    public ResponseEntity<CommonResponse> unexpectedRollbackException(UnexpectedRollbackException e) {
        return responseService.getErrorResponse(HttpStatus.BAD_REQUEST, "Failed to create security question.");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CommonResponse> runtimeException(RuntimeException e) {
        return responseService.getErrorResponse(HttpStatus.CONFLICT, e.getMessage());
    }
}
