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

    @Column(nullable = false, length = 30)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 10)
    private String nickname;

    @Column(nullable = false, length = 13)
    private String telephone;

    @Column(nullable = false, length = 10)
    private String role; // ROLE_USER, ROLE_ADMIN
}
