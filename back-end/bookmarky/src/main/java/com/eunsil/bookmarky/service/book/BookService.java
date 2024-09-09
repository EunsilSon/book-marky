package com.eunsil.bookmarky.service.book;

import com.eunsil.bookmarky.config.SecurityUtil;
import com.eunsil.bookmarky.domain.dto.BookDTO;
import com.eunsil.bookmarky.domain.entity.Book;
import com.eunsil.bookmarky.domain.entity.User;
import com.eunsil.bookmarky.domain.entity.BookRecord;
import com.eunsil.bookmarky.repository.BookRepository;
import com.eunsil.bookmarky.repository.BookRecordRepository;
import com.eunsil.bookmarky.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@Service
public class BookService {

    private final SecurityUtil securityUtil;
    private final NaverOpenApiSearch naverOpenApiSearch;
    private final OpenApiResponseParser openApiResponseParser;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BookRecordRepository bookRecordRepository;

    /**
     * Open Api 를 통해 제목으로 책 검색
     */
    public List<BookDTO> searchBooksByTitleFromOpenApi(String title, int page) {
        String response = naverOpenApiSearch.searchBooksByTitle(title, page); // 오픈 API 응답 결과
        return openApiResponseParser.jsonToBookList(response);
    }

    /**
     * Open Api 를 통해 고유번호(ISBN) 으로 책 검색
     */
    public BookDTO searchBookByIsbnFromOpenApi(String isbn) throws Exception {
        String response = naverOpenApiSearch.searchBookByIsbn(isbn);
        return openApiResponseParser.xmlToBook(response);
    }

    /**
     * 책 상세 정보 조회
     */
    public BookDTO getBookDetails(long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Book Not Found"));

        return BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .link(book.getLink())
                .image(book.getImage())
                .isbn(book.getIsbn())
                .description(book.getDescription())
                .build();
    }

    /**
     * 책 저장
     */
    @Transactional
    public Book addNewBook(String isbn) throws Exception {
        return bookRepository.save(searchBookByIsbnFromOpenApi(isbn).toEntity());
    }

}