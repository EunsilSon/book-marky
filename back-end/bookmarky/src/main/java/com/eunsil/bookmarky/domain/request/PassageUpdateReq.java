package com.eunsil.bookmarky.domain.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PassageUpdateReq {

    @NotNull
    private Long passageId;

    @NotNull
    @Size(max = 1000)
    private String content;

}
