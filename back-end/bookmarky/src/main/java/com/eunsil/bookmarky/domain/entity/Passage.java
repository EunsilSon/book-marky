package com.eunsil.bookmarky.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
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
@Filter(name = "deletedPassageFilter", condition = "isDeleted = :isDeleted")
public class Passage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int pageNum;

    @NotNull
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotNull
    private Long bookId;

    @NotNull
    private Long userId;

    @Builder.Default
    private Boolean isDeleted = Boolean.FALSE; // BigInt (0, 1)

}
