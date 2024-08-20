package com.eunsil.bookmarky.domain.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String nickname;
    private String telephone;
    private Long secureQuestionId;
}
