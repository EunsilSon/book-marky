package com.eunsil.bookmarky.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class ResponseUtil {

    public static <T> ApiResponse<T> createSuccessResponse(T data) {
        return new ApiResponse<>(HttpStatus.OK, "Success", data);
    }

    public static ApiResponse<String> createSuccessResponse() {
        return new ApiResponse<>(HttpStatus.OK, "Success");
    }

    public static <T> ApiResponse<T> createErrorResponse(HttpStatusCode status, String message) {
        return new ApiResponse<>(status, message);
    }
}
