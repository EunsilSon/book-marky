package com.eunsil.bookmarky.service.book;

import com.eunsil.bookmarky.config.SecurityUtil;
import com.eunsil.bookmarky.config.filter.FilterManager;
import com.eunsil.bookmarky.domain.dto.BookDTO;
import com.eunsil.bookmarky.domain.dto.BookSimpleDTO;
import com.eunsil.bookmarky.domain.entity.Book;
import com.eunsil.bookmarky.domain.entity.User;
import com.eunsil.bookmarky.domain.entity.BookRecord;
import com.eunsil.bookmarky.repository.BookRepository;
import com.eunsil.bookmarky.repository.BookRecordRepository;
import com.eunsil.bookmarky.repository.PassageRepository;
import com.eunsil.bookmarky.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookService {

    private static final String DEFAULT_BOOK_TITLE_LIST_TYPE = "id";
    private static final int DEFAULT_BOOK_TITLE_LIST_SIZE = 10;

    private final SecurityUtil securityUtil;
    private final NaverOpenApiSearch naverOpenApiSearch;
    private final OpenApiResponseParser openApiResponseParser;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BookRecordRepository bookRecordRepository;
    private final PassageRepository passageRepository;
    private final FilterManager filterManager;


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
     * 책 저장
     */
    @Transactional
    public void addBook(BookDTO bookDTO) {
        User user = userRepository.findByUsername(securityUtil.getCurrentUsername());
        Book book = bookRepository.save(bookDTO.toEntity());

        BookRecord bookRecord = BookRecord.builder()
                .user(user)
                .book(book)
                .createdAt(LocalDate.now())
                .build();
        bookRecordRepository.save(bookRecord);
    }


    /**
     * 책 저장 기록 삭제
     */
    @Transactional
    public boolean deleteBookById(String id) {
        Long userId = userRepository.findByUsername(securityUtil.getCurrentUsername()).getId();
        bookRecordRepository.deleteByBookIdAndUserId(Long.valueOf(id), userId);
        passageRepository.deleteByBookIdAndUserId(Long.valueOf(id), userId);
        return true;
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
     * 저장된 책 목록 조회
     */
    public List<BookDTO> getSavedBooks(int page, String order, int size) {
        User user = userRepository.findByUsername(securityUtil.getCurrentUsername());
        Pageable pageable = PageRequest.of(page, size, Sort.by(order).descending());

        filterManager.enableFilter("deletedBookRecordFilter", "isDeleted", false);
        Page<BookRecord> userBookRecords = bookRecordRepository.findByUserId(user.getId(), pageable);
        filterManager.disableFilter("deletedBookRecordFilter");

        return userBookRecords.stream()
                .map(bookRecord -> {
                    Book book = bookRecord.getBook();
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
                })
                .collect(Collectors.toList());
    }


    /**
     * 저장된 책의 제목만 조회
     */
    public List<BookSimpleDTO> getSavedBookTitles(int page) {
        filterManager.enableFilter("deletedBookRecordFilter", "isDeleted", false);
        List<BookDTO> bookList = getSavedBooks(page, DEFAULT_BOOK_TITLE_LIST_TYPE, DEFAULT_BOOK_TITLE_LIST_SIZE); // 책의 모든 정보
        filterManager.disableFilter("deletedBookRecordFilter");

        return bookList.stream()
                .map(book -> new BookSimpleDTO(book.getId(), book.getTitle()))
                .collect(Collectors.toList());
    }

}