package com.eunsil.bookmarky.repository;

import com.eunsil.bookmarky.domain.entity.BookRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BookRecordRepository extends JpaRepository<BookRecord, Long> {

    boolean existsByBookId(Long bookId);

    int deleteByBookIdAndUserId(Long bookId, Long userId);

    Page<BookRecord> findByUserId(Long userId, Pageable pageable);
}
