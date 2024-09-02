package com.eunsil.bookmarky.repository;

import com.eunsil.bookmarky.domain.entity.Passage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface PassageRepository extends JpaRepository<Passage, Long> {

    Page<Passage> findByUserIdAndBookId(Long userId, Long bookId, Pageable pageable);

    Page<Passage> findByUserId(Long userId, Pageable pageable);

    List<Passage> findByIsDeletedTrueAndDeletedAtBefore(LocalDate date);

}
