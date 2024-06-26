package com.eunsil.bookmarky.controller;

import com.eunsil.bookmarky.domain.entity.Book;
import com.eunsil.bookmarky.service.BookService;
import com.eunsil.bookmarky.service.api.NaverOpenApiSearchBook;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.bson.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * @param title 책 제목
     * @return 제목과 관련성이 높은 책 정보 10개
     */
    @GetMapping("/{title}/{page}")
    public List<Book> search(@PathVariable String title, @PathVariable int page) {
        return bookService.search(title, page);
    }

}
