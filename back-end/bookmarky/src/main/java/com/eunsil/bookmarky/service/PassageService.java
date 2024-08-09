package com.eunsil.bookmarky.service;

import com.eunsil.bookmarky.domain.entity.Book;
import com.eunsil.bookmarky.domain.entity.Passage;
import com.eunsil.bookmarky.domain.entity.User;
import com.eunsil.bookmarky.domain.request.PassageReq;
import com.eunsil.bookmarky.domain.request.PassageUpdateReq;
import com.eunsil.bookmarky.domain.response.PassageListRes;
import com.eunsil.bookmarky.repository.PassageRepository;
import com.eunsil.bookmarky.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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


    /**
     * 구절 수정
     * @param passageUpdateReq 구절 id, 수정된 content
     * @return 수정 여부
     */
    public ResponseEntity update(PassageUpdateReq passageUpdateReq) {

        Passage passage = passageRepository.findById(passageUpdateReq.getPassageId()).orElse(null);

        passage.setContent(passageUpdateReq.getContent());
        passage.setDate(LocalDate.now());
        passageRepository.save(passage);

        return ResponseEntity.status(HttpStatus.OK).header("result").body("success");
    }


    /**
     * 구절 상세 조회
     * @param id
     * @return
     */
    public ResponseEntity<Passage> get(Long id) {

        Passage passage = passageRepository.findById(id).orElse(null);

        if (passage == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return new ResponseEntity<>(passage, HttpStatus.OK);
    }


    /**
     * 구절 목록 조회
     * - 저장한 구절을 전체적으로 조회하기 위함
     * @param username 유저 이름
     * @param bookId 책 id
     * @return 쪽수, 구절 내용
     */
    public ResponseEntity<List<PassageListRes>> getList(String username, Long bookId) {

        User user = userRepository.findByUsername(username);
        List<Passage> passagesList = passageRepository.findByUserIdAndBookId(user.getId(), bookId);

        // 필요한 값만 추출 (pageNum, content)
        List<PassageListRes> passageListResList = passagesList.stream()
                .map(passage -> new PassageListRes(passage.getPageNum(), passage.getContent()))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(passageListResList);
    }

}
