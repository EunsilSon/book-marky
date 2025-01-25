package com.eunsil.bookmarky.domain.vo;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PasswordVO {
    @Pattern(regexp = "^[a-z\\d._]+@[a-z._]+\\.[a-z]{2,6}$") // 이메일
    private String username;

    @Size(min = 9, max = 15, message = "비밀번호는 최소 9자리, 최대 15자리입니다.")
    private String password;

    private String token;
}