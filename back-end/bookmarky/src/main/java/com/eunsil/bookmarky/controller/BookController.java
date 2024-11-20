package com.eunsil.bookmarky.controller;

import com.eunsil.bookmarky.domain.dto.BookDTO;
import com.eunsil.bookmarky.response.ApiResponse;
import com.eunsil.bookmarky.response.ResponseUtil;
import com.eunsil.bookmarky.service.book.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class BookController {

    private final BookService bookService;

    @GetMapping("/books")
    public ApiResponse<List<BookDTO>> searchBooksByTitle(@RequestParam String title, @RequestParam int page) {
        return ResponseUtil.createSuccessResponse(bookService.searchBooksByTitleFromOpenApi(title, page));
    }

    @GetMapping("/book/{id}")
    public ApiResponse<BookDTO> getDetails(@PathVariable Long id) {
        Optional<BookDTO> bookDTO = bookService.getBookDetails(id);
        if (bookDTO.isEmpty()) {
            return ResponseUtil.createErrorResponse(HttpStatus.NOT_FOUND,"Book Not Found.");
        }
        return ResponseUtil.createSuccessResponse(bookDTO.get());
    }

}
