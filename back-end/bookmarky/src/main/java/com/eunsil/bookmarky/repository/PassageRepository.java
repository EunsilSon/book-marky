package com.eunsil.bookmarky.repository;

import com.eunsil.bookmarky.domain.entity.Passage;
import jakarta.transaction.Transactional;
import jdk.jfr.Timestamp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;


@Repository
public interface PassageRepository extends JpaRepository<Passage, Long> {

    Page<Passage> findByUserIdAndBookId(Long userId, Long bookId, Pageable pageable);

    Page<Passage> findByUserId(Long userId, Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM Passage p WHERE p.isDeleted = true AND p.deletedAt < :date")
    void deleteByIsDeletedTrueAndDeletedAtBefore(LocalDate date);

}
