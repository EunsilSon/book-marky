package com.eunsil.bookmarky.domain.request;

import lombok.Getter;

@Getter
public class PasswordResetReq {

    private String username;
    private String password;
    private String token;

    public PasswordResetReq(String username, String password, String token) {
        this.username = username;
        this.password = password;
        this.token = token;
    }

}
