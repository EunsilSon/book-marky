package com.eunsil.bookmarky.repository;

import com.eunsil.bookmarky.domain.entity.BookRecord;
import com.eunsil.bookmarky.domain.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface BookRecordRepository extends JpaRepository<BookRecord, Long> {
    Page<BookRecord> findByUserId(Long userId, Pageable pageable);

    BookRecord findByBookId(Long bookId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Passage p WHERE p.isDeleted = true AND p.deletedAt < :date")
    void deleteByIsDeletedTrueAndDeletedAtBefore(LocalDate date);

    void deleteByBookIdAndUserId(Long bookId, Long userId);

    Long countByIsDeletedAndUser(Boolean isDeleted, User user);

    Boolean existsByBookIdAndUserId(Long BookId, Long userId);
}
