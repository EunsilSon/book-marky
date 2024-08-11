package com.eunsil.bookmarky.controller;

import com.eunsil.bookmarky.domain.vo.BookVO;
import com.eunsil.bookmarky.domain.entity.Book;
import com.eunsil.bookmarky.service.book.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
     * @param title 책 제목
     * @return Book 을 담은 리스트
     */
    @GetMapping("/{title}/{page}")
    public List<Book> search(@PathVariable String title, @PathVariable int page) {
        return bookService.search(title, page);
    }


    /**
     * 저장한 책의 제목만 조회
     * @param username
     * @param page
     * @return
     */
    @GetMapping("/title/{username}")
    public ResponseEntity<Map<Long, String>> getByTitle(@PathVariable String username, @RequestParam(defaultValue = "0") int page) {
        return new ResponseEntity<>(bookService.getTitleList(username, page), HttpStatus.OK);
    }


    /**
     * 저장한 책 목록 조회
     * @param username
     * @return book 리스트
     */
    @GetMapping("/{username}")
    public ResponseEntity<List<Book>> get(@PathVariable String username, @RequestParam(defaultValue = "0") int page) {
        return new ResponseEntity<>(bookService.getList(username, page, 6), HttpStatus.OK);
    }


    /**
     * 책 상세 정보 조회
     * @param id Book Id
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
     * @param bookVO 사용자 이름, 책 isbn
     * @return 삭제 여부
     */
    @DeleteMapping("/")
    public boolean delete(@RequestBody BookVO bookVO) {
        return bookService.delete(bookVO);
    }


}
