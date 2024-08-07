package com.eunsil.bookmarky.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ResponseService {

    public ResponseEntity<CommonResponse> getErrorResponse(HttpStatusCode code, String message) {
        CommonResponse response = CommonResponse.builder()
                .result(false)
                .code(code.value())
                .message(message)
                .build();

        return new ResponseEntity<>(response, code);
    }
}
