package com.eunsil.bookmarky.controller;

import com.eunsil.bookmarky.domain.dto.BookDTO;
import com.eunsil.bookmarky.service.book.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BookController {

    private final BookService bookService;

    /**
     * 제목으로 책 검색
     */
    @GetMapping("/books")
    public ResponseEntity<List<BookDTO>> searchBooksByTitle(@RequestParam String title, @RequestParam int page) {
        return new ResponseEntity<>(bookService.searchBooksByTitleFromOpenApi(title, page), HttpStatus.OK);
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


}
