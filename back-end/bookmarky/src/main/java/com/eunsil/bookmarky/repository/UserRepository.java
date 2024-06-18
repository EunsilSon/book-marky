package com.eunsil.bookmarky.repository;

import com.eunsil.bookmarky.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);
    User findByUsername(String username);

}
