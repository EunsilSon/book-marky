package com.eunsil.bookmarky.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String telephone;

    @Column(nullable = false)
    private String role; // ROLE_USER, ROLE_ADMIN

    @Column(nullable = false)
    private Long secureQuestionId;

}
