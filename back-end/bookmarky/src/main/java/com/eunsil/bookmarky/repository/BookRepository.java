package com.eunsil.bookmarky.repository;

import com.eunsil.bookmarky.domain.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Book findByIsbn(String isbn);

    Boolean existsByIsbn(String isbn);
}
