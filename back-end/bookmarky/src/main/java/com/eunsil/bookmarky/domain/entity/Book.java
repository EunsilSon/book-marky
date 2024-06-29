package com.eunsil.bookmarky.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true) // JSON → 객체 변환 시 알 수 없는 필드 무시
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(columnDefinition = "TEXT")
    @JsonProperty("description")
    private String description;

}
