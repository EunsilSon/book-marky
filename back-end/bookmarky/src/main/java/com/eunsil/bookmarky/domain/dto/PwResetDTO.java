package com.eunsil.bookmarky.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PwResetDTO {

    private String username;
    private String token;

}
