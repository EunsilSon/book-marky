package com.eunsil.bookmarky.repository;

import com.eunsil.bookmarky.domain.entity.Passage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PassageRepository extends JpaRepository<Passage, Long> {

    Page<Passage> findByUserIdAndBookIdAndIsDeleted(Long userId, Long bookId, Boolean isDeleted, Pageable pageable);

    List<Passage> findByUserIdAndIsDeleted(Long userId, Boolean isDeleted);
}
