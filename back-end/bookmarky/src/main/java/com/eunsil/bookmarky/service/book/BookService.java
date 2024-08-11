package com.eunsil.bookmarky.service.book;

import com.eunsil.bookmarky.domain.dto.BookDTO;
import com.eunsil.bookmarky.domain.vo.BookVO;
import com.eunsil.bookmarky.domain.entity.Book;
import com.eunsil.bookmarky.domain.entity.User;
import com.eunsil.bookmarky.domain.entity.BookRecord;
import com.eunsil.bookmarky.repository.BookRepository;
import com.eunsil.bookmarky.repository.BookRecordRepository;
import com.eunsil.bookmarky.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class BookService {

    private final NaverOpenApiSearch naverOpenApiSearch;
    private final ParsingService parsingService;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BookRecordRepository bookRecordRepository;

    @Autowired
    public BookService(NaverOpenApiSearch naverOpenApiSearch
            , ParsingService parsingService
            , BookRepository bookRepository
            , BookRecordRepository bookRecordRepository
            , UserRepository userRepository) {
        this.naverOpenApiSearch = naverOpenApiSearch;
        this.parsingService = parsingService;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.bookRecordRepository = bookRecordRepository;
    }

    /**
     * 책 검색
     * - open api 를 통해 책 검색 후 Book 객체로 변환해 전달
     *
     * @param title 제목
     * @param page 가져올 페이지 번호
     * @return Book 을 담은 리스트
     */
    public List<Book> search(String title, int page) {
        String response = naverOpenApiSearch.book(title, page); // 오픈 API 응답 결과
        return parsingService.jsonToBookList(response);
    }


    /**
     * open api 를 통해 isbn 으로 책 검색
     *
     * @param isbn 책 isbn
     * @return Book 객체
     * @throws Exception open api 의 xml 형식 응답 값을 파싱하면서 발생 가능
     */
    public Book searchWithIsbn(String isbn) throws Exception {
        String response = naverOpenApiSearch.bookDetail(isbn);
        return parsingService.xmlToBook(response);
    }


    /**
     * 저장한 이력이 없는 책 등록
     * - 구절 생성할 때 새로운 책 정보가 함께 저장됨
     *
     * @param username 유저 이메일
     * @param book Book 객체
     * @return Book id
     */
    @Transactional
    public Long add(String username, Book book) {

        User user = userRepository.findByUsername(username);

        bookRepository.save(book); // 책 정보 저장

        // 책 기록 저장
        BookRecord bookRecord = BookRecord.builder()
                .user(user)
                .book(book)
                .date(LocalDate.now())
                .build();
        bookRecordRepository.save(bookRecord);

        return bookRepository.findByIsbn(book.getIsbn()).getId();
    }


    /**
     * 책 삭제
     *
     * @param bookVO 사용자 이름, 책 isbn
     * @return 삭제 여부
     */
    @Transactional
    public boolean delete(BookVO bookVO) {

        User user = userRepository.findByUsername(bookVO.getUsername());
        Book book = bookRepository.findByIsbn(bookVO.getIsbn());

        bookRecordRepository.deleteByBookIdAndUserId(book.getId(), user.getId());
        return true;
    }


    /**
     * 저장한 책 목록 조회
     *
     * @param username 유저 이메일
     * @param page 페이지 번호
     * @param size 반환 개수
     * @return Book 리스트
     */
    public List<Book> getList(String username, int page, int size) {

        User user = userRepository.findByUsername(username);

        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        Page<BookRecord> userBookRecords = bookRecordRepository.findByUserId(user.getId(), pageable);

        return userBookRecords.stream()
                .map(bookRecord -> bookRepository.findByIsbn(bookRecord.getBook().getIsbn()))
                .collect(Collectors.toList());

    }


    /**
     * 저장한 책 제목만 리스트로 반환 (페이징)
     *
     * @param username 유저 이메일
     * @param page 페이지 번호
     * @return BookDTO 리스트
     */
    public List<BookDTO> getTitleList(String username, int page) {

        List<Book> bookList = getList(username, page, 10); // 책의 모든 정보

        return bookList.stream()
                .map(book -> new BookDTO(book.getId(), book.getTitle()))
                .collect(Collectors.toList());

    }


    /**
     * 책 상세 정보 조회
     *
     * @param id 책 id
     * @return Book 객체
     */
    public Book getInfo(long id) {
        return bookRepository.findById(id).orElseThrow(null);
    }


}
