package com.eunsil.bookmarky.controller;

import com.eunsil.bookmarky.domain.dto.AddBookReq;
import com.eunsil.bookmarky.domain.entity.Book;
import com.eunsil.bookmarky.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
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
     * @return Book 을 담은 리스트
     */
    @GetMapping("/{title}/{page}")
    public List<Book> search(@PathVariable String title, @PathVariable int page) {
        return bookService.search(title, page);
    }

    /**
     * 책 기록 저장
     * @param addBookReq
     * @return
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    @PostMapping("/")
    public boolean add(@RequestBody AddBookReq addBookReq) throws ParserConfigurationException, IOException, SAXException {
        return bookService.add(addBookReq);
    }
}
