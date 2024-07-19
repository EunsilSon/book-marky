package com.eunsil.bookmarky.repository;

import com.eunsil.bookmarky.domain.entity.UserBookRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBookRecordRepository extends JpaRepository<UserBookRecord, Long> {

    boolean existsByBookId(Long bookId);

    int deleteByBookIdAndUserId(Long bookId, Long userId);

    List<UserBookRecord> findByUserId(Long userId);
}
