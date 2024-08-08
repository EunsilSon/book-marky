package com.eunsil.bookmarky.repository;

import com.eunsil.bookmarky.domain.entity.Passage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassageRepository extends JpaRepository<Passage, Long> {
}