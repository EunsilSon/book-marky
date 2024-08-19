package com.eunsil.bookmarky.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 30)
    private String author;

    @Column(nullable = false, length = 100)
    private String publisher;

    @Column(nullable = false, length = 100)
    private String link;

    @Column(nullable = false, length = 100)
    private String image;

    @Column(nullable = false, length = 20)
    private String isbn;

    @Column(nullable = false, length = 1000)
    private String description;
}
