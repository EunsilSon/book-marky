package com.eunsil.bookmarky.service;

import com.eunsil.bookmarky.domain.dto.BookDTO;
import com.eunsil.bookmarky.domain.entity.Book;
import com.eunsil.bookmarky.repository.BookRepository;
import com.eunsil.bookmarky.service.openApi.NaverOpenApiSearch;
import com.eunsil.bookmarky.service.openApi.OpenApiResponseParser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class BookService {

    private final NaverOpenApiSearch naverOpenApiSearch;
    private final OpenApiResponseParser openApiResponseParser;
    private final BookRepository bookRepository;

    public Optional<BookDTO> getBookDetails(long id) {
        return bookRepository.findById(id)
                .map(book -> BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .link(book.getLink())
                .image(book.getImage())
                .isbn(book.getIsbn())
                .description(book.getDescription())
                .build());
    }

    public List<BookDTO> searchBooksByTitleFromOpenApi(String title, int page) {
        String response = naverOpenApiSearch.searchBooksByTitle(title, page); // 오픈 API 응답 결과
        return openApiResponseParser.jsonToBookList(response);
    }

    public BookDTO searchBookByIsbnFromOpenApi(String isbn) {
        String response = naverOpenApiSearch.searchBookByIsbn(isbn);
        return openApiResponseParser.xmlToBook(response);
    }

    @Transactional
    public Book addNewBook(String isbn) {
        return bookRepository.save(searchBookByIsbnFromOpenApi(isbn).toEntity());
    }

}