package com.eunsil.bookmarky.service;

import com.eunsil.bookmarky.domain.entity.Book;
import com.eunsil.bookmarky.domain.entity.Passage;
import com.eunsil.bookmarky.domain.entity.User;
import com.eunsil.bookmarky.domain.vo.PassageVO;
import com.eunsil.bookmarky.domain.vo.PassageUpdateVO;
import com.eunsil.bookmarky.domain.dto.PassageListDTO;
import com.eunsil.bookmarky.repository.PassageRepository;
import com.eunsil.bookmarky.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PassageService {

    private final FilterManager filterManager;
    private final UserRepository userRepository;
    private final BookService bookService;
    private PassageRepository passageRepository;

    public PassageService(FilterManager filterManager, PassageRepository passageRepository, UserRepository userRepository, BookService bookService) {
        this.filterManager = filterManager;
        this.passageRepository = passageRepository;
        this.userRepository = userRepository;
        this.bookService = bookService;
    }


    /**
     * 특정 책의 구절 생성
     * - DB에 없는 책의 경우, 구절 생성 시 새로운 책 정보가 함께 DB에 저장됨
     * @param passageVO
     * @return
     * @throws Exception 책 정보가 없을 때 검색을 위해 open api 호출 후 응답 값을 XML 로 변환하는 과정에서 발생 가능
     */
    public ResponseEntity add(PassageVO passageVO) throws Exception {

        // 책 정보
        Long bookId;

        if (passageVO.getBookId() == null) { // 저장한 이력이 없는 책
            Book book = bookService.searchWithIsbn(passageVO.getIsbn());
            bookId = bookService.add(passageVO.getUsername(), book); // 책 정보와 기록 저장
        } else { // 이미 저장된 책
            bookId = passageVO.getBookId();
        }

        // 구절 생성
        Passage passage = Passage.builder()
                .bookId(bookId)
                .userId(userRepository.findByUsername(passageVO.getUsername()).getId())
                .content(passageVO.getContent())
                .date(LocalDate.now())
                .build();

        passageRepository.save(passage);

        return ResponseEntity.status(HttpStatus.OK).header("result").body("success");
    }


    /**
     * 구절 수정
     * @param passageUpdateVO 구절 id, 수정된 content
     * @return 수정 여부
     */
    public ResponseEntity update(PassageUpdateVO passageUpdateVO) {

        Passage passage = passageRepository.findById(passageUpdateVO.getPassageId()).orElse(null);

        passage.setContent(passageUpdateVO.getContent());
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
     * 구절 목록 조회 (10개씩, passage id 기준 내림차순)
     * - 저장한 구절을 전체적으로 조회하기 위함
     * - 페이징: 반환 개수 10개 고정
     * @param username 유저 이름
     * @param bookId 책 id
     * @return 쪽수, 구절 내용
     */
    public ResponseEntity<List<PassageListDTO>> getList(String username, Long bookId, int page) {

        filterManager.enableFilter("deletedPassageFilter", "isDeleted", false); // 필터 활성화

        User user = userRepository.findByUsername(username);
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        Page<Passage> passagesList = passageRepository.findByUserIdAndBookIdAndIsDeleted(user.getId(), bookId, false, pageable);

        filterManager.disableFilter("deletedPassageFilter"); // 필터 비활성화

        // 필요한 값만 추출 (pageNum, content)
        List<PassageListDTO> passageListResList = passagesList.stream()
                .map(passage -> new PassageListDTO(passage.getPageNum(), passage.getContent()))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(passageListResList);
    }


    /**
     * 구절 삭제
     * - soft delete: 30일간 보존 후 영구 삭제
     * @param id 구절 id
     * @return 삭제 여부
     */
    @Transactional
    public ResponseEntity delete(Long id) {
        passageRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).header("result").body("success");
    }


    /**
     * 최근 삭제 내역 조회 (30일 보관)
     * @return 삭제된 구절 리스트
     */
    @Transactional
    public List<Passage> getAllDeleted(String username) {

        filterManager.enableFilter("deletedPassageFilter", "isDeleted", true); // 필터 활성화

        User user = userRepository.findByUsername(username);
        List<Passage> deletedPassageList = passageRepository.findByUserIdAndIsDeleted(user.getId(), true);

        filterManager.disableFilter("deletedPassageFilter"); // 필터 비활성화

        return deletedPassageList;
    }


}
