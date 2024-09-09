package com.eunsil.bookmarky.controller;

import com.eunsil.bookmarky.domain.dto.BookDTO;
import com.eunsil.bookmarky.domain.dto.BookSimpleDTO;
import com.eunsil.bookmarky.service.book.BookRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class BookRecordController {

    private static final int DEFAULT_BOOK_SIZE = 6;
    private final BookRecordService bookRecordService;

    /**
     * 책 목록 조회
     */
    @GetMapping("/books/saved")
    public ResponseEntity<List<BookDTO>> getSavedBooks(@RequestParam(defaultValue = "id") String order, @RequestParam(defaultValue = "0") int page) {
        return new ResponseEntity<>(bookRecordService.getSavedBooks(page, order, DEFAULT_BOOK_SIZE), HttpStatus.OK);
    }

    /**
     * 책 제목만 조회
     */
    @GetMapping("/books/titles")
    public ResponseEntity<List<BookSimpleDTO>> getSavedBookTitles(@RequestParam(defaultValue = "0") int page) {
        return new ResponseEntity<>(bookRecordService.getSavedBookTitles(page), HttpStatus.OK);
    }

    /**
     * 저장한 책 삭제
     */
    @DeleteMapping("/book/{id}")
    public ResponseEntity<String> deleteRecord(@PathVariable String id) {
        if (bookRecordService.deleteBookRecordById(id)) {
            return new ResponseEntity<>("Ok", HttpStatus.OK);
        }
        return new ResponseEntity<>("Book Record is not existed", HttpStatus.BAD_REQUEST);
    }

}
