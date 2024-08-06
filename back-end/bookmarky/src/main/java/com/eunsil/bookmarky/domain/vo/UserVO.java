package com.eunsil.bookmarky.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserDTO {

    @NotBlank
    @Pattern(regexp = "^[a-z\\d._]+@[a-z._]+\\.[a-z]{2,6}$") // 이메일
    private String username;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d{5,})(?=.*[~!@#$%^&*)]).{2,}$", message = "비밀번호는 최소 9자리, 최대 15자리입니다.") // 대소문자, 숫자 5개 이상, 특수문자 2개 이상
    @Size(min = 9, max = 15)
    private String password;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z가-힣]*$", message = "닉네임은 최소 3자리, 최대 8자리입니다.") // 대소문자, 한글
    @Size(min = 3, max = 8)
    private String nickname;

    @NotBlank
    @Pattern(regexp = "\\d{2,3}-\\d{3,4}-\\d{4}+$") // 숫자, 하이픈
    private String telephone;

}
