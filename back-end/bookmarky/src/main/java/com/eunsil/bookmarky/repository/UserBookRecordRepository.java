package com.eunsil.bookmarky.repository;

import com.eunsil.bookmarky.domain.entity.Book;
import com.eunsil.bookmarky.domain.entity.User;
import com.eunsil.bookmarky.domain.entity.UserBookRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface UserBookRecordRepository extends JpaRepository<UserBookRecord, Long> {

    boolean existsByBookId(Long bookId);

    int deleteByBookId(Long bookId);

    List<UserBookRecord> findByUserId(Long userId);
}
