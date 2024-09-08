package com.eunsil.bookmarky.controller;

import com.eunsil.bookmarky.domain.dto.BookDTO;
import com.eunsil.bookmarky.domain.dto.BookSimpleDTO;
import com.eunsil.bookmarky.service.book.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BookController {

    private static final int DEFAULT_BOOK_SIZE = 6;
    private final BookService bookService;


    /**
     * 제목으로 책 검색
     */
    @GetMapping("/books")
    public ResponseEntity<List<BookDTO>> searchBooksByTitle(@RequestParam String title, @RequestParam int page) {
        return new ResponseEntity<>(bookService.searchBooksByTitleFromOpenApi(title, page), HttpStatus.OK);
    }

    /**
     * 저장된 책 목록 조회
     */
    @GetMapping("/books/saved")
    public ResponseEntity<List<BookDTO>> getSavedBooks(@RequestParam(defaultValue = "id") String order, @RequestParam(defaultValue = "0") int page) {
        return new ResponseEntity<>(bookService.getSavedBooks(page, order, DEFAULT_BOOK_SIZE), HttpStatus.OK);
    }

    /**
     * 저장된 책의 제목만 조회
     */
    @GetMapping("/books/titles")
    public ResponseEntity<List<BookSimpleDTO>> getSavedBookTitles(@RequestParam(defaultValue = "0") int page) {
        return new ResponseEntity<>(bookService.getSavedBookTitles(page), HttpStatus.OK);
    }

    /**
     * 책 상세 정보 조회
     */
    @GetMapping("/book/{id}")
    public ResponseEntity<BookDTO> getDetails(@PathVariable Long id) {
        BookDTO bookDTO = bookService.getBookDetails(id);
        if (bookDTO == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(bookDTO);
        }
    }

    /**
     * 책 삭제
     */
    @DeleteMapping("/book")
    public ResponseEntity<String> delete(@RequestParam String id) {
        if (bookService.deleteBookById(id)) {
            return new ResponseEntity<>("Ok", HttpStatus.OK);
        }
        return new ResponseEntity<>("Book is not existed", HttpStatus.BAD_REQUEST);
    }

}
