package com.eunsil.bookmarky.domain.dto;

import lombok.Getter;

@Getter
public class PasswordResetRes {

    private String username;
    private String token;

    public PasswordResetRes(String username, String token) {
        this.username = username;
        this.token = token;
    }

}
