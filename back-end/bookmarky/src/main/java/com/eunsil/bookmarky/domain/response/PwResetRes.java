package com.eunsil.bookmarky.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PwResetRes {

    private String username;
    private String token;

}
