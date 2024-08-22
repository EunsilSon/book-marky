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
     * 책 검색
     *
     * @param title 제목
     * @param page 페이지 번호
     * @return Book 리스트
     */
    @GetMapping("/books")
    public List<BookDTO> search(@RequestParam String title, @RequestParam int page) {
        return bookService.search(title, page);
    }


    /**
     * 저장한 책 목록 조회
     *
     * @param page 페이지 번호 (기본 값: 0)
     * @param order 정렬 기준 (기본 값: id)
     * @return BookDTO 리스트
     */
    @GetMapping("/books")
    public ResponseEntity<List<BookDTO>> getList(@RequestParam(defaultValue = "id") String order, @RequestParam(defaultValue = "0") int page) {
        return new ResponseEntity<>(bookService.getList(page, order, DEFAULT_BOOK_SIZE), HttpStatus.OK);
    }


    /**
     * 저장한 책의 제목만 조회
     *
     * @param page 페이지 번호
     * @return 책 id 와 제목을 담은 BookSimpleDTO 리스트
     */
    @GetMapping("/books/titles")
    public ResponseEntity<List<BookSimpleDTO>> getTitleList(@RequestParam(defaultValue = "0") int page) {
        return new ResponseEntity<>(bookService.getTitleList(page), HttpStatus.OK);
    }


    /**
     * 책 상세 정보 조회
     *
     * @param id 책 id
     * @return Book 객체
     */
    @GetMapping("/book/{id}")
    public ResponseEntity<BookDTO> getInfo(@PathVariable long id) {

        BookDTO bookDTO = bookService.getInfo(id);

        if (bookDTO == null) {
            return ResponseEntity.notFound().build(); // 404
        } else {
            return ResponseEntity.ok().body(bookDTO);
        }

    }


    /**
     * 책 삭제
     *
     * @param isbn 책 고유 번호
     * @return 삭제 여부
     */
    @DeleteMapping("/book")
    public ResponseEntity delete(@RequestParam String isbn) {
        return ResponseEntity.ok(bookService.delete(isbn));
    }


}
