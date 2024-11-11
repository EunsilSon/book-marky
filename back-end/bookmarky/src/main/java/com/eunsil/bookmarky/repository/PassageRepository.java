package com.eunsil.bookmarky.repository;

import com.eunsil.bookmarky.domain.entity.Book;
import com.eunsil.bookmarky.domain.entity.Passage;
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
public interface PassageRepository extends JpaRepository<Passage, Long> {
    Page<Passage> findByUserIdAndBookId(Long userId, Long bookId, Pageable pageable);

    Page<Passage> findByUserId(Long userId, Pageable pageable);

    void deleteByBookIdAndUserId(Long bookId, Long userId);

    boolean existsByBookIdAndIsDeletedFalse(Long bookId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Passage p WHERE p.isDeleted = true AND p.deletedAt < :date")
    void deleteByIsDeletedTrueAndDeletedAtBefore(LocalDate date);

    Long countByIsDeletedAndBookAndUser(Boolean isDeleted, Book book, User user);
}
