package com.eunsil.bookmarky.domain.dto;

import com.eunsil.bookmarky.domain.entity.Book;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true) // JSON → 객체 변환 시 알 수 없는 필드 무시
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BookDTO {
    private Long id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("author")
    private String author;

    @JsonProperty("publisher")
    private String publisher;

    @JsonProperty("link")
    private String link;

    @JsonProperty("image")
    private String image;

    @JsonProperty("isbn")
    private String isbn;

    @JsonProperty("description")
    private String description;

    public Book toEntity() {
        return new Book(id, title, author, publisher, link, image, isbn, description);
    }
}
