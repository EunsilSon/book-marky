package com.eunsil.bookmarky.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PasswordResetRes {

    private String username;
    private String token;

}
