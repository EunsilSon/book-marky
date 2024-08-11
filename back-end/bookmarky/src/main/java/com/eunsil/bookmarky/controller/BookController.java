package com.eunsil.bookmarky.controller;

import com.eunsil.bookmarky.domain.dto.BookDTO;
import com.eunsil.bookmarky.domain.vo.BookVO;
import com.eunsil.bookmarky.domain.entity.Book;
import com.eunsil.bookmarky.service.book.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    /**
     * 책 검색
     *
     * @param title 제목
     * @param page 페이지 번호
     * @return Book 리스트
     */
    @GetMapping("/{title}/{page}")
    public List<Book> search(@PathVariable String title, @PathVariable int page) {
        return bookService.search(title, page);
    }


    /**
     * 저장한 책 목록 조회
     *
     * @param username 유저 이메일
     * @param page 페이지 번호
     * @return Book 리스트
     */
    @GetMapping("/{username}")
    public ResponseEntity<List<Book>> getList(@PathVariable String username, @RequestParam(defaultValue = "0") int page) {
        return new ResponseEntity<>(bookService.getList(username, page, 6), HttpStatus.OK);
    }


    /**
     * 저장한 책의 제목만 조회
     *
     * @param username 유저 이메일
     * @param page 페이지 번호
     * @return 책 id 와 제목을 담은 BookDTO 리스트
     */
    @GetMapping("/title/{username}")
    public ResponseEntity<List<BookDTO>> getTitleList(@PathVariable String username, @RequestParam(defaultValue = "0") int page) {
        return new ResponseEntity<>(bookService.getTitleList(username, page), HttpStatus.OK);
    }


    /**
     * 책 상세 정보 조회
     *
     * @param id 책 id
     * @return Book 객체
     */
    @GetMapping("/info/{id}")
    public ResponseEntity<Book> getInfo(@PathVariable long id) {

        Book book = bookService.getInfo(id);

        if (book == null) {
            return ResponseEntity.notFound().build(); // 404
        } else {
            return ResponseEntity.ok().body(book);
        }

    }


    /**
     * 책 삭제
     *
     * @param bookVO 유저 이메일과 책 id
     * @return 삭제 여부
     */
    @DeleteMapping("/")
    public ResponseEntity delete(@Valid @RequestBody BookVO bookVO) {
        return ResponseEntity.ok(bookService.delete(bookVO));
    }


}
