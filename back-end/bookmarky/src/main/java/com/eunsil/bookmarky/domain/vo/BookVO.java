package com.eunsil.bookmarky.domain.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class BookVO {

    @NotNull
    private String username;

    @NotNull
    private String isbn;

}
