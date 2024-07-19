package com.eunsil.bookmarky.service;

import com.eunsil.bookmarky.domain.dto.BookReq;
import com.eunsil.bookmarky.domain.entity.Book;
import com.eunsil.bookmarky.domain.entity.User;
import com.eunsil.bookmarky.domain.entity.UserBookRecord;
import com.eunsil.bookmarky.repository.BookRepository;
import com.eunsil.bookmarky.repository.UserBookRecordRepository;
import com.eunsil.bookmarky.repository.UserRepository;
import com.eunsil.bookmarky.service.api.NaverOpenApiSearchBook;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.print.Pageable;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;


@Service
public class BookService {

    private final NaverOpenApiSearchBook naverOpenApiSearchBook;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final UserBookRecordRepository userBookRecordRepository;

    @Autowired
    public BookService(NaverOpenApiSearchBook naverOpenApiSearchBook
            , BookRepository bookRepository
            , UserRepository userRepository
            , UserBookRecordRepository userBookRecordRepository) {
        this.naverOpenApiSearchBook = naverOpenApiSearchBook;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.userBookRecordRepository = userBookRecordRepository;
    }

    /**
     * 책 검색
     * @param title 제목
     * @param page 가져올 페이지 번호
     * @return Book 을 담은 리스트
     */
    public List<Book> search(String title, int page) {
        String responseBody = naverOpenApiSearchBook.book(title, page); // 오픈 API 응답 결과
        List<Book> bookList = new ArrayList<>();

        // Book 객체로 변환 후 리스트에 담음
        try {
            // JSON 을 Java 로 변환하고 원하는 속성의 값만 추출
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node = objectMapper.readTree(responseBody);
            JsonNode items = node.path("items");

            // 추출한 값을 순차적으로 접근
            Iterator<JsonNode> elements = items.elements();
            while (elements.hasNext()) {
                JsonNode bookNode = elements.next();
                Book book = objectMapper.treeToValue(bookNode, Book.class); // Book 객체로 변환
                bookList.add(book);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return bookList;
    }


    /**
     * 책 저장
     * @param bookReq 사용자 이름, 책 isbn
     * @return 저장 유무
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public boolean add(BookReq bookReq) throws Exception {
        Book book = getOrCreateBook(bookReq.getIsbn()); // 저장할 책
        return saveUserBookRecord(bookReq.getUsername(), book); // 사용자의 책 기록
    }


    /**
     * DB 에서 책 조회 또는 Open API 검색
     * @param isbn
     * @return 책 정보를 담은 Book
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    private Book getOrCreateBook(String isbn) throws Exception {

        // DB에 책 정보가 있을 때
        if (bookRepository.existsByIsbn(isbn)) {
            return bookRepository.findByIsbn(isbn);
        }

        // DB에 책 정보가 없을 때
        String response = naverOpenApiSearchBook.bookDetail(isbn); // 네이버 오픈 api 책 상세 조회
        Book book = xmlToBook(response);
        bookRepository.save(book); // DB 저장

        return book;
    }


    /**
     * 사용자의 책 기록 저장
     * @param username
     * @param book
     * @return 저장 유무
     */
    private boolean saveUserBookRecord(String username, Book book) {

        User user = userRepository.findByUsername(username);

        if (!userBookRecordRepository.existsByBookId(book.getId())) { // 책 존재 유무 확인
            UserBookRecord userBookRecord = UserBookRecord.builder()
                    .user(user)
                    .book(book)
                    .date(LocalDate.now())
                    .build();

            userBookRecordRepository.save(userBookRecord);
            return true;
        }

        return false;
    }


    /**
     * XML 을 Book 객체로 변환
     * @param xml
     * @return Book
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    private Book xmlToBook(String xml) throws Exception {

        // xml 파싱 -> 함수로 빼기
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xml)));

        // 책 정보 추출
        List<String> bookInfo = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            if (i == 4 || i == 6) {
                continue;
            }

            String info = document
                    .getElementsByTagName("item")
                    .item(0)
                    .getChildNodes()
                    .item(i) // 가져올 값
                    .getTextContent();

            bookInfo.add(info); // title 0, link 1, image 2, author 3, publisher 5, isbn 7, description 8;
        }

        // book 객체 반환
        return Book.builder()
                .title(bookInfo.get(0))
                .link(bookInfo.get(1))
                .image(bookInfo.get(2))
                .author(bookInfo.get(3))
                .publisher(bookInfo.get(4))
                .isbn(bookInfo.get(5))
                .description(bookInfo.get(6))
                .build();
    }


    /**
     * 책 기록 조회
     * @param username
     * @return Book 리스트
     */
    public List<Book> get(String username) {

        User user = userRepository.findByUsername(username);
        List<UserBookRecord> userBookRecords = userBookRecordRepository.findByUserId(user.getId()); // TODO: Pageable 기능 추가

        List<Book> bookList = new ArrayList<>(); // book 객체만 담은 리스트
        for (UserBookRecord userBookRecord : userBookRecords) {
            bookList.add(userBookRecord.getBook());
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

        userBookRecordRepository.deleteByBookIdAndUserId(book.getId(), user.getId());
        return true;
    }

}
