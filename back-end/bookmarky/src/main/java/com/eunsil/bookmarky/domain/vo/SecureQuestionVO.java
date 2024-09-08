package com.eunsil.bookmarky.domain.vo;

import lombok.Getter;

@Getter
public class SecureQuestionVO {
    private String username;
    private Long secureQuestionId;
    private String answer;
}
