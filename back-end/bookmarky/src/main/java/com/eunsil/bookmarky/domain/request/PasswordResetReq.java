package com.eunsil.bookmarky.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PasswordResetReq {

    @NotBlank
    @Pattern(regexp = "^[a-z\\d._]+@[a-z._]+\\.[a-z]{2,6}$") // 이메일
    private String username;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d{5,})(?=.*[~!@#$%^&*)]).{2,}$", message = "비밀번호는 최소 9자리, 최대 15자리입니다.") // 대소문자, 숫자 5개 이상, 특수문자 2개 이상
    @Size(min = 9, max = 15)
    private String password;

    @NotBlank
    private String token;

}