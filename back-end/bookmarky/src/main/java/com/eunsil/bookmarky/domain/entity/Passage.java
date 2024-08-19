package com.eunsil.bookmarky.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SQLDelete(sql = "UPDATE passage SET is_deleted = true WHERE id = ?")
@FilterDef(name = "deletedPassageFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
@Filter(name = "deletedPassageFilter", condition = "is_deleted = :isDeleted")
public class Passage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Long bookId;

    @NotNull
    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private int pageNum;

    @Size(max = 1000, message = "최대 1,000자까지 가능합니다.")
    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Builder.Default
    private Boolean isDeleted = Boolean.FALSE; // BigInt (0, 1)
}
