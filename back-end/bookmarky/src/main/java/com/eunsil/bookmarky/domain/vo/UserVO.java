package com.eunsil.bookmarky.domain.vo;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserVO {
    @Pattern(regexp = "^[a-z\\d._]+@[a-z._]+\\.[a-z]{2,6}$", message = "아이디는 이메일 형식입니다.")
    private String username;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d{5,})(?=.*[~!@#$%^&*)]).{2,}$", message = "비밀번호는 대소문자, 숫자 5개 이상, 특수문자 2개 이상입니다.")
    @Size(min = 9, max = 15, message = "비밀번호는 최소 9자, 최대 15자입니다.")
    private String password;

    @Pattern(regexp = "^[a-zA-Z가-힣]*$", message = "닉네임은 영문과 한글만 가능합니다.")
    @Size(min = 3, max = 10, message = "닉네임은 최소 3자, 최대 10자입니다.")
    private String nickname;

    @Pattern(regexp = "\\d{2,3}-\\d{3,4}-\\d{4}+$", message = "010-0000-0000 형식입니다.")
    private String telephone;

    private Long secureQuestionId;

    @Size(min = 1, max = 50, message = "답변은 최소 1자, 최대 50자입니다.")
    private String answerContent;
}
