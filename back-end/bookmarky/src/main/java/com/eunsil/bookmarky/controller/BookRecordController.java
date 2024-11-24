package com.eunsil.bookmarky.controller;

import com.eunsil.bookmarky.domain.dto.BookDTO;
import com.eunsil.bookmarky.domain.dto.BookSimpleDTO;
import com.eunsil.bookmarky.global.response.ApiResponse;
import com.eunsil.bookmarky.global.response.ResponseUtil;
import com.eunsil.bookmarky.service.BookRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BookRecordController {

    private static final int DEFAULT_BOOK_SIZE = 6;
    private final BookRecordService bookRecordService;

    @GetMapping("/books/saved")
    public ApiResponse<List<BookDTO>> getSavedBooks(@RequestParam(defaultValue = "id") String order, @RequestParam(defaultValue = "0") int page) {
        return ResponseUtil.createSuccessResponse(bookRecordService.getSavedBooks(page, order, DEFAULT_BOOK_SIZE));
    }

    @GetMapping("/books/titles")
    public ApiResponse<List<BookSimpleDTO>> getSavedBookTitles(@RequestParam(defaultValue = "0") int page) {
        return ResponseUtil.createSuccessResponse(bookRecordService.getSavedBookTitles(page));
    }

    @DeleteMapping("/book/{id}")
    public ApiResponse<String> delete(@PathVariable String id) {
        if (bookRecordService.deleteByBookId(id)) {
            return ResponseUtil.createSuccessResponse();
        }
        return ResponseUtil.createErrorResponse(HttpStatus.NOT_FOUND, "Book Record Not Found.");
    }

    @DeleteMapping("/book-all/{id}")
    public ApiResponse<String> deleteAll(@PathVariable String id) {
        if (bookRecordService.deleteAllWithPassages(id)) {
            return ResponseUtil.createSuccessResponse();
        }
        return ResponseUtil.createErrorResponse(HttpStatus.NOT_FOUND, "Book Record Not Found");
    }

    @GetMapping("/book/count")
    public ApiResponse<Long> getCount() {
        return ResponseUtil.createSuccessResponse(bookRecordService.getCount());
    }

}
