package com.eunsil.bookmarky.exception;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CommonResponse {

    boolean result;
    int code;
    String message;

}
