package com.eunsil.bookmarky.repository;

import com.eunsil.bookmarky.domain.entity.Book;
import com.eunsil.bookmarky.domain.entity.UserBookRecords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBookRecordsRepository extends JpaRepository<UserBookRecords, Long> {

    boolean existsByBookId(Long bookId);

}
