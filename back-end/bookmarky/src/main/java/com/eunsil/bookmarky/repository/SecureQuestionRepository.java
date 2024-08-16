package com.eunsil.bookmarky.repository;

import com.eunsil.bookmarky.domain.entity.SecureQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecureQuestionRepository extends JpaRepository<SecureQuestion, Long> {
}
