package com.eunsil.bookmarky.service;

import com.eunsil.bookmarky.config.SecurityUtil;
import com.eunsil.bookmarky.config.filter.FilterManager;
import com.eunsil.bookmarky.domain.dto.BookDTO;
import com.eunsil.bookmarky.domain.dto.BookSimpleDTO;
import com.eunsil.bookmarky.domain.entity.Book;
import com.eunsil.bookmarky.domain.entity.BookRecord;
import com.eunsil.bookmarky.domain.entity.User;
import com.eunsil.bookmarky.repository.BookRecordRepository;
import com.eunsil.bookmarky.repository.PassageRepository;
import com.eunsil.bookmarky.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookRecordService {

    private static final String DEFAULT_BOOK_TITLE_LIST_TYPE = "id";
    private static final int DEFAULT_BOOK_TITLE_LIST_SIZE = 10;

    private final FilterManager filterManager;
    private final SecurityUtil securityUtil;
    private final UserRepository userRepository;
    private final BookRecordRepository bookRecordRepository;
    private final PassageRepository passageRepository;

    /**
     * 책 기록 생성
     */
    public boolean createBookRecord(Book book, User user) {
        try {
            BookRecord bookRecord = BookRecord.builder()
                    .user(user)
                    .book(book)
                    .createdAt(LocalDate.now())
                    .build();
            bookRecordRepository.save(bookRecord);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 책 기록 삭제
     */
    @Transactional
    public boolean deleteBookRecordById(String id) {
        Long userId = userRepository.findByUsername(securityUtil.getCurrentUsername()).orElseThrow(() -> new RuntimeException("Username Not Found")).getId();
        bookRecordRepository.deleteByBookIdAndUserId(Long.valueOf(id), userId);
        passageRepository.deleteByBookIdAndUserId(Long.valueOf(id), userId);
        return true;
    }


    /**
     * 저장된 책 목록 조회
     */
    public List<BookDTO> getSavedBooks(int page, String order, int size) {
        User user = userRepository.findByUsername(securityUtil.getCurrentUsername()).orElseThrow(() -> new RuntimeException("Username Not Found"));
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
                .map(book -> new BookSimpleDTO(book.getIsbn(), book.getTitle()))
                .collect(Collectors.toList());
    }


    /**
     * 삭제한 지 30일 지난 책 기록 영구 제거
     * :매일 자정에 실행됨
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void dailyCleanUpOfDeletedPassages() {
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        bookRecordRepository.deleteByIsDeletedTrueAndDeletedAtBefore(thirtyDaysAgo);
        log.info("삭제 30일 경과된 BookRecord 영구 삭제");
    }

    /**
     * 저장한 책 개수
     */
    public Long getCount() {
        User user = userRepository.findByUsername(securityUtil.getCurrentUsername()).orElseThrow(() -> new RuntimeException("Username Not Found"));
        return bookRecordRepository.countByIsDeletedAndUser(false, user);
    }

}
