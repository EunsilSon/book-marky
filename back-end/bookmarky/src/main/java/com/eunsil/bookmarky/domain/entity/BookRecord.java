package com.eunsil.bookmarky.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@SQLDelete(sql = "UPDATE book_record SET is_deleted = true, deleted_at = NOW() WHERE id = ?")
public class BookRecord { // 사용자가 읽은 책 저장
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(nullable = false)
    private LocalDate createdAt;

    private LocalDate deletedAt;

    @Builder.Default
    private Boolean isDeleted = Boolean.FALSE; // BigInt (0, 1)
}
