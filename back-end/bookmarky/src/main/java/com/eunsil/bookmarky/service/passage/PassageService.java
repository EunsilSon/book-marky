package com.eunsil.bookmarky.service.passage;

<<<<<<< HEAD
import com.eunsil.bookmarky.config.FilterManager;
=======
import com.eunsil.bookmarky.config.filter.FilterManager;
>>>>>>> back
import com.eunsil.bookmarky.domain.entity.Book;
import com.eunsil.bookmarky.domain.entity.Passage;
import com.eunsil.bookmarky.domain.entity.User;
import com.eunsil.bookmarky.domain.vo.PassageVO;
import com.eunsil.bookmarky.domain.vo.PassageUpdateVO;
import com.eunsil.bookmarky.domain.dto.PassageListDTO;
import com.eunsil.bookmarky.repository.BookRepository;
import com.eunsil.bookmarky.repository.PassageRepository;
import com.eunsil.bookmarky.repository.UserRepository;
import com.eunsil.bookmarky.service.book.BookService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class PassageService {

    private final FilterManager filterManager;
    private final UserRepository userRepository;
    private final BookService bookService;
    private final BookRepository bookRepository;
    private PassageRepository passageRepository;

    public PassageService(FilterManager filterManager
            , PassageRepository passageRepository
            , UserRepository userRepository
            , BookService bookService, BookRepository bookRepository) {
        this.filterManager = filterManager;
        this.passageRepository = passageRepository;
        this.userRepository = userRepository;
        this.bookService = bookService;
        this.bookRepository = bookRepository;
    }


    /**
     * 특정 책의 구절 생성
     *
     * - DB에 없는 책의 경우, 구절 생성 시 새로운 책 정보가 함께 DB에 저장됨
     * @param passageVO isSaved, isbn, username, content
     * @return 생성 여부
     * @throws Exception 책 정보가 없을 때 검색을 위해 open api 호출 후 응답 값을 XML 로 변환하는 과정에서 발생 가능
     */
    @Transactional
    public boolean add(PassageVO passageVO) throws Exception {

        // 책 정보
        Long newBookId;

        if (passageVO.getIsSaved()) { // 이미 저장된 책
            newBookId = bookRepository.findByIsbn(passageVO.getIsbn()).getId();
        } else { // 저장한 이력이 없는 책
            Book book = bookService.searchByOpenApiWithIsbn(passageVO.getIsbn());
            newBookId = bookService.add(passageVO.getUsername(), book); // 책 정보와 기록 저장
        }

        // 구절 생성
        Passage passage = Passage.builder()
                .bookId(newBookId)
                .userId(userRepository.findByUsername(passageVO.getUsername()).getId())
                .content(passageVO.getContent())
                .date(LocalDate.now())
                .build();

        passageRepository.save(passage);
        return true;
    }


    /**
     * 구절 수정
     *
     * @param passageUpdateVO 구절 id, 수정된 content
     * @return 수정 여부
     */
    @Transactional
    public boolean update(PassageUpdateVO passageUpdateVO) {

        Passage passage = passageRepository.findById(passageUpdateVO.getPassageId()).orElseThrow(() -> new NoSuchElementException("Passage Not Found"));
        passage.setContent(passageUpdateVO.getContent());
        passage.setDate(LocalDate.now());

        passageRepository.save(passage);
        return true;
    }


    /**
     * 구절 삭제
     * - soft delete: 30일간 보존 후 영구 삭제
     * @param id 구절 id
     * @return 삭제 여부
     */
    @Transactional
    public boolean delete(Long id) {
        passageRepository.deleteById(id);
        return true;
    }


    /**
     * 구절 상세 조회
     *
     * @param id 구절 id
     * @return Passage 객체
     */
    public Passage get(Long id) {
        return passageRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Passage Not Found"));
    }


    /**
     * 구절 목록 조회
     *
     * - 저장한 구절을 전체적으로 조회하기 위함
     * - 페이징: 10개씩, passage id 기준 내림차순, 반환 개수 10개 고정
     * @param username 유저 이메일
     * @param bookId 책 id
     * @return PassageListDTO 리스트 (pageNum, content)
     */
    public List<PassageListDTO> getList(String username, Long bookId, int page) {

        // 필터 활성화
        filterManager.enableFilter("deletedPassageFilter", "isDeleted", false);


        User user = userRepository.findByUsername(username);
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        Page<Passage> passagesList = passageRepository.findByUserIdAndBookIdAndIsDeleted(user.getId(), bookId, false, pageable);

        // 필터 비활성화
        filterManager.disableFilter("deletedPassageFilter");


        return passagesList.stream()
                .map(passage -> new PassageListDTO(passage.getPageNum(), passage.getContent(), passage.getBookId()))
                .collect(Collectors.toList());

    }


    /**
     * 최근 삭제 내역 조회 (30일 보관)
     *
     * @param username 유저 이메일
     * @param page 페이지 번호
     * @return PassageListDTO 리스트 (pageNum, content)
     */
    public List<PassageListDTO> getAllDeleted(String username, int page) {

        // 필터 활성화
        filterManager.enableFilter("deletedPassageFilter", "isDeleted", true);


        User user = userRepository.findByUsername(username);
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        Page<Passage> deletedPassageList = passageRepository.findByUserIdAndIsDeleted(user.getId(), true, pageable);


        // 필터 비활성화
        filterManager.disableFilter("deletedPassageFilter");


        return deletedPassageList.stream()
                .map(passage -> new PassageListDTO(passage.getPageNum()
                        , passage.getContent()
                        , passage.getBookId()))
                .collect(Collectors.toList());

    }


}
