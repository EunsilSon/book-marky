package com.eunsil.bookmarky.domain.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PassageVO {
    @NotBlank
    private String isbn;

    @Size(max = 1000, message = "입력 가능 길이는 최대 1000자리입니다.")
    private String content;

    @NotBlank
    private String pageNum;
}
