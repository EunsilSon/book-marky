package com.eunsil.bookmarky.repository.user;

import com.eunsil.bookmarky.domain.entity.SecureAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecureAnswerRepository extends JpaRepository<SecureAnswer, Long> {

    SecureAnswer findByUserId(Long userId);

}
