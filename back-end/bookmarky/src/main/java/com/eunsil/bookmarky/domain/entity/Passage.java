package com.eunsil.bookmarky.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SQLDelete(sql = "UPDATE passage SET is_deleted = true, deleted_at = NOW() WHERE id = ?")
@FilterDef(name = "deletedPassageFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
@Filter(name = "deletedPassageFilter", condition = "is_deleted = :isDeleted")
public class Passage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private String pageNum;

    @Column(nullable = false)
    private LocalDate createdAt;

    private LocalDate deletedAt;

    @Builder.Default
    private Boolean isDeleted = Boolean.FALSE; // BigInt (0, 1)
}
