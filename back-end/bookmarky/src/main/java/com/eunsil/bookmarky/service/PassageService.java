package com.eunsil.bookmarky.service;

import com.eunsil.bookmarky.global.config.security.SecurityUtil;
import com.eunsil.bookmarky.global.config.filter.FilterManager;
import com.eunsil.bookmarky.domain.dto.PassageDTO;
import com.eunsil.bookmarky.domain.entity.Book;
import com.eunsil.bookmarky.domain.entity.BookRecord;
import com.eunsil.bookmarky.domain.entity.Passage;
import com.eunsil.bookmarky.domain.entity.User;
import com.eunsil.bookmarky.domain.vo.PassageVO;
import com.eunsil.bookmarky.domain.vo.PassageUpdateVO;
import com.eunsil.bookmarky.repository.BookRecordRepository;
import com.eunsil.bookmarky.repository.BookRepository;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PassageService {

    private static final int DEFAULT_PASSAGE_SIZE = 10;

    private final SecurityUtil securityUtil;
    private final FilterManager filterManager;
    private final BookService bookService;
    private final BookRecordService bookRecordService;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final PassageRepository passageRepository;
    private final BookRecordRepository bookRecordRepository;

    private Book getOrCreateBook(String isbn, User user) {
        Book book;
        if (bookRepository.existsByIsbn(isbn)) {
            book = bookRepository.findByIsbn(isbn);
        } else {
            book = bookService.addNewBook(isbn);
            bookRecordService.create(book, user);
        }
        return book;
    }

    @Transactional
    public boolean createPassage(PassageVO passageVO) {
        User user = userRepository.findByUsername(securityUtil.getCurrentUsername()).orElseThrow(() -> new UsernameNotFoundException("Username Not Found."));

        try {
            Passage passage = Passage.builder()
                    .book(getOrCreateBook(passageVO.getIsbn(), user))
                    .user(user)
                    .content(passageVO.getContent())
                    .pageNum(passageVO.getPageNum())
                    .createdAt(LocalDate.now())
                    .build();
            passageRepository.save(passage);
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    @Transactional
    public boolean updatePassage(PassageUpdateVO passageUpdateVO) {
        Passage passage = passageRepository.findById(passageUpdateVO.getPassageId()).orElseThrow(() -> new NoSuchElementException("Passage Not Found."));
        passage.setContent(passageUpdateVO.getContent());
        passage.setPageNum(passageUpdateVO.getPageNum());
        passage.setCreatedAt(LocalDate.now());
        return true;
    }

    public PassageDTO getPassageDetails(Long id) {
        Passage passage = passageRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Passage Not Found."));

        return PassageDTO.builder()
                .id(passage.getId())
                .userId(passage.getUser().getId())
                .bookId(passage.getBook().getId())
                .content(passage.getContent())
                .pageNum(passage.getPageNum())
                .createdAt(passage.getCreatedAt())
                .build();
    }

    public List<PassageDTO> getPassages(Long bookId, String order, int page) {
        User user = userRepository.findByUsername(securityUtil.getCurrentUsername()).orElseThrow(() -> new UsernameNotFoundException("Username Not Found."));
        Pageable pageable = PageRequest.of(page, DEFAULT_PASSAGE_SIZE, Sort.by(order).descending());

        filterManager.enableFilter("deletedPassageFilter", "isDeleted", false);
        Page<Passage> passagesList = passageRepository.findByUserIdAndBookId(user.getId(), bookId, pageable);
        filterManager.disableFilter("deletedPassageFilter");

        return passagesList.stream()
                .map(passage -> new PassageDTO(
                        passage.getId()
                        , passage.getUser().getId()
                        , passage.getBook().getId()
                        , bookRepository.findByIsbn(passage.getBook().getIsbn()).getTitle()
                        , passage.getPageNum()
                        , passage.getContent()
                        , passage.getCreatedAt()))
                .collect(Collectors.toList());
    }

    public List<PassageDTO> getDeletedPassages(int page) {
        User user = userRepository.findByUsername(securityUtil.getCurrentUsername()).orElseThrow(() -> new UsernameNotFoundException("Username Not Found."));
        Pageable pageable = PageRequest.of(page, DEFAULT_PASSAGE_SIZE, Sort.by("id").descending());

        filterManager.enableFilter("deletedPassageFilter", "isDeleted", true);
        Page<Passage> deletedPassageList = passageRepository.findByUserId(user.getId(), pageable);
        filterManager.disableFilter("deletedPassageFilter");

        return deletedPassageList.stream()
                .map(passage -> new PassageDTO(
                        passage.getId()
                        , passage.getUser().getId()
                        , passage.getBook().getId()
                        , bookRepository.findByIsbn(passage.getBook().getIsbn()).getTitle()
                        , passage.getPageNum()
                        , passage.getContent()
                        , passage.getCreatedAt()))
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean deletePassage(Long id) {
        Passage passage = passageRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Passage Not Found."));
        passageRepository.delete(passage);
        return true;
    }

    @Transactional
    public boolean restoreDeletedPassage(String id) {
        try {
            Passage passage = passageRepository.findById(Long.valueOf(id)).orElseThrow(() -> new NoSuchElementException("Passage Not Found."));
            passage.setIsDeleted(false);
            passage.setDeletedAt(null);

            BookRecord bookRecord = bookRecordRepository.findByBookId(passage.getBook().getId());
            bookRecord.setIsDeleted(false);
            bookRecord.setDeletedAt(null);
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    /**
     * 삭제한 지 30일 지난 구절 영구 제거
     * :매일 자정에 실행됨
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void dailyCleanUpOfDeletedPassages() {
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        passageRepository.deleteByIsDeletedTrueAndDeletedAtBefore(thirtyDaysAgo);
        log.info("삭제 30일 경과된 Passage 영구 삭제");
    }

}