package com.eunsil.bookmarky.exception;

import org.springframework.http.ResponseEntity;
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
     * VO 유효성 검증 실패했을 때
     * @param e MethodArgumentNotValidException
     * @return CommonResponse
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return responseService.getErrorResponse(e.getStatusCode(), e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }
}
