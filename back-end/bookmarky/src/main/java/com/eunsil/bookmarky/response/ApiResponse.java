package com.eunsil.bookmarky.response;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;

    public ApiResponse(HttpStatusCode status, String message) {
        this.status = status.value();
        this.message = message;
        this.data = (T) "empty";
    }

    public ApiResponse(HttpStatusCode status, String message, T data) {
        this.status = status.value();
        this.message = message;
        this.data = (data == null) ? (T) "empty" : data;
    }
}