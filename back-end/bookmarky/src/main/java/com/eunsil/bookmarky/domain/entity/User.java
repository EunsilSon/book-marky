package com.eunsil.bookmarky.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username; // email type
    private String password;
    private String nickname;
    private String role; // ROLE_USER, ROLE_ADMIN
}
