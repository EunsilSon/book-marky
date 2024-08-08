package com.eunsil.bookmarky.service;

import com.eunsil.bookmarky.domain.entity.Book;
import com.eunsil.bookmarky.domain.entity.Passage;
import com.eunsil.bookmarky.domain.request.PassageReq;
import com.eunsil.bookmarky.repository.PassageRepository;
import com.eunsil.bookmarky.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PassageService {

    private final UserRepository userRepository;
    private final BookService bookService;
    private PassageRepository passageRepository;

    public PassageService(PassageRepository passageRepository, UserRepository userRepository, BookService bookService) {
        this.passageRepository = passageRepository;
        this.userRepository = userRepository;
        this.bookService = bookService;
    }


    /**
     * 특정 책의 구절 생성
     * - DB에 없는 책의 경우, 구절 생성 시 새로운 책 정보가 함께 DB에 저장됨
     * @param passageReq
     * @return
     * @throws Exception 책 정보가 없을 때 검색을 위해 open api 호출 후 응답 값을 XML 로 변환하는 과정에서 발생 가능
     */
    public ResponseEntity add(PassageReq passageReq) throws Exception {

        // 책 정보
        Long bookId;

        if (passageReq.getBookId() == null) { // 저장한 이력이 없는 책
            Book book = bookService.searchWithIsbn(passageReq.getIsbn());
            bookId = bookService.add(passageReq.getUsername(), book); // 책 정보와 기록 저장
        } else { // 이미 저장된 책
            bookId = passageReq.getBookId();
        }

        // 구절 생성
        Passage passage = Passage.builder()
                .bookId(bookId)
                .userId(userRepository.findByUsername(passageReq.getUsername()).getId())
                .content(passageReq.getContent())
                .date(LocalDate.now())
                .build();

        passageRepository.save(passage);

        return ResponseEntity.status(HttpStatus.OK).header("result").body("success");
    }

}
