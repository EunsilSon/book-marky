package com.eunsil.bookmarky.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;

@AllArgsConstructor
@Builder
public class PassageDTO {
    public Long id;
    public Long userId;
    public Long bookId;
    public Integer pageNum;
    public String content;
    private LocalDate createdAt;
}
