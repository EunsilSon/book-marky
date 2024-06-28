package com.eunsil.bookmarky.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class UserBookRecords {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    private LocalDateTime date;
}
