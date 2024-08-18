package com.eunsil.bookmarky.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true) // JSON → 객체 변환 시 알 수 없는 필드 무시
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("title")
    @Column(nullable = false, length = 100)
    private String title;

    @JsonProperty("author")
    @Column(nullable = false, length = 30)
    private String author;

    @JsonProperty("publisher")
    @Column(nullable = false, length = 100)
    private String publisher;

    @JsonProperty("link")
    @Column(nullable = false, length = 100)
    private String link;

    @JsonProperty("image")
    @Column(nullable = false, length = 100)
    private String image;

    @JsonProperty("isbn")
    @Column(nullable = false, length = 20)
    private String isbn;

    @JsonProperty("description")
    @Column(nullable = false, length = 1000)
    private String description;
}
