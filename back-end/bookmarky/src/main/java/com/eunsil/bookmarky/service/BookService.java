package com.eunsil.bookmarky.service;

import com.eunsil.bookmarky.domain.request.BookReq;
import com.eunsil.bookmarky.domain.entity.Book;
import com.eunsil.bookmarky.domain.entity.User;
import com.eunsil.bookmarky.domain.entity.BookRecord;
import com.eunsil.bookmarky.repository.BookRepository;
import com.eunsil.bookmarky.repository.BookRecordRepository;
import com.eunsil.bookmarky.repository.UserRepository;
import com.eunsil.bookmarky.service.api.NaverOpenApiSearchBook;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class BookService {

    private final NaverOpenApiSearchBook naverOpenApiSearchBook;
    private final ParsingService parsingService;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BookRecordRepository bookRecordRepository;

    @Autowired
    public BookService(NaverOpenApiSearchBook naverOpenApiSearchBook
            , ParsingService parsingService
            , BookRepository bookRepository
            , BookRecordRepository bookRecordRepository
            , UserRepository userRepository) {
        this.naverOpenApiSearchBook = naverOpenApiSearchBook;
        this.parsingService = parsingService;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.bookRecordRepository = bookRecordRepository;
    }

    /**
     * 책 검색
     * - OPEN API 를 통해 책 검색 후 Book 객체로 변환해 전달
     * @param title 제목
     * @param page 가져올 페이지 번호
     * @return Book 을 담은 리스트
     */
    public List<Book> search(String title, int page) {
        String response = naverOpenApiSearchBook.book(title, page); // 오픈 API 응답 결과
        return parsingService.jsonToBookList(response);
    }


    /**
     * 저장한 이력이 없는 책 등록
     * - 구절 등록할 때 새로운 책 정보가 함께 등록됨
     * @param
     * @return 저장 유무
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    @Transactional
    public boolean add(String username, String isbn) throws Exception {

        User user = userRepository.findByUsername(username);

        // 오픈 API 를 통해 책 상세 조회
        String response = naverOpenApiSearchBook.bookDetail(isbn);
        Book book = parsingService.xmlToBook(response);
        bookRepository.save(book); // 책 정보 저장


        // 책 기록 등록
        BookRecord bookRecord = BookRecord.builder()
                .user(user)
                .book(book)
                .date(LocalDate.now())
                .build();
        bookRecordRepository.save(bookRecord);

        return true;
    }


    /**
     * 책 기록 조회
     * @param username
     * @return Book 리스트
     */
    public List<Book> get(String username, int page) {

        User user = userRepository.findByUsername(username);

        Pageable pageable = PageRequest.of(page, 6, Sort.by("date").descending()); // pageable 객체 생성
        Page<BookRecord> userBookRecords = bookRecordRepository.findByUserId(user.getId(), pageable);

        List<Book> bookList = new ArrayList<>();
        for (BookRecord bookRecord : userBookRecords) {
            bookList.add(bookRepository.findByIsbn(bookRecord.getBook().getIsbn()));
        }

        return bookList;
    }


    /**
     * 책 상세 정보 조회
     * @param id Book Id
     * @return Book 객체
     */
    public Book getInfo(long id) {

        Optional<Book> book = bookRepository.findById(id);

        if (!book.isEmpty()) {
            return book.get();
        } else {
            System.out.println("[BookController]: Book not found");
            return null;
        }

    }


    /**
     * 책 삭제
     * @param bookReq 사용자 이름, 책 isbn
     * @return 삭제 여부
     */
    @Transactional
    public boolean delete(BookReq bookReq) {

        User user = userRepository.findByUsername(bookReq.getUsername());
        Book book = bookRepository.findByIsbn(bookReq.getIsbn());

        bookRecordRepository.deleteByBookIdAndUserId(book.getId(), user.getId());
        return true;
    }

}
