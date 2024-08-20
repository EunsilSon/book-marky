package com.eunsil.bookmarky.domain.vo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PassageUpdateVO {

    @NotNull
    private Long passageId;

    @NotNull
    @Size(max = 1000)
    private String content;

    @NotNull
    private Integer pageNum;

}
