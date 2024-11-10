package com.eunsil.bookmarky.repository.user;

import com.eunsil.bookmarky.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    boolean existsByNickname(String nickname);

    boolean existsByTelephone(String telephone);

    User findByUsername(String username);

}
