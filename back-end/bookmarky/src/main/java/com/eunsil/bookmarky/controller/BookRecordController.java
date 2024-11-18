package com.eunsil.bookmarky.controller;

import com.eunsil.bookmarky.domain.dto.BookDTO;
import com.eunsil.bookmarky.domain.dto.BookSimpleDTO;
import com.eunsil.bookmarky.service.BookRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BookRecordController {

    private static final int DEFAULT_BOOK_SIZE = 6;
    private final BookRecordService bookRecordService;

    @GetMapping("/books/saved")
    public ResponseEntity<List<BookDTO>> getSavedBooks(@RequestParam(defaultValue = "id") String order, @RequestParam(defaultValue = "0") int page) {
        return new ResponseEntity<>(bookRecordService.getSavedBooks(page, order, DEFAULT_BOOK_SIZE), HttpStatus.OK);
    }

    @GetMapping("/books/titles")
    public ResponseEntity<List<BookSimpleDTO>> getSavedBookTitles(@RequestParam(defaultValue = "0") int page) {
        return new ResponseEntity<>(bookRecordService.getSavedBookTitles(page), HttpStatus.OK);
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        if (bookRecordService.deleteByBookId(id)) {
            return new ResponseEntity<>("Ok", HttpStatus.OK);
        }
        return new ResponseEntity<>("Book Record is not existed", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/book-all/{id}")
    public ResponseEntity<String> deleteAll(@PathVariable String id) {
        if (bookRecordService.deleteAllWithPassages(id)) {
            return new ResponseEntity<>("Ok", HttpStatus.OK);
        }
        return new ResponseEntity<>("Book Record is not existed", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/book/count")
    public ResponseEntity<Long> getCount(@RequestParam String username) {
        return new ResponseEntity<>(bookRecordService.getCount(username), HttpStatus.OK);
    }

}
