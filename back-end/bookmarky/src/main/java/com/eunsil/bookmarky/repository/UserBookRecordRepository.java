package com.eunsil.bookmarky.repository;

import com.eunsil.bookmarky.domain.entity.UserBookRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserBookRecordRepository extends JpaRepository<UserBookRecord, Long> {

    boolean existsByBookId(Long bookId);

    int deleteByBookIdAndUserId(Long bookId, Long userId);

    Page<UserBookRecord> findByUserIdOrderByDateDesc(Long userId, Pageable pageable);
}
