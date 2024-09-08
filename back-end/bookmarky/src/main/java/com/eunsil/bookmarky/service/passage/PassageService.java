package com.eunsil.bookmarky.service.passage;

import com.eunsil.bookmarky.config.SecurityUtil;
import com.eunsil.bookmarky.config.filter.FilterManager;
import com.eunsil.bookmarky.domain.dto.BookDTO;
import com.eunsil.bookmarky.domain.dto.PassageDTO;
import com.eunsil.bookmarky.domain.entity.Passage;
import com.eunsil.bookmarky.domain.entity.User;
import com.eunsil.bookmarky.domain.vo.PassageVO;
import com.eunsil.bookmarky.domain.vo.PassageUpdateVO;
import com.eunsil.bookmarky.repository.BookRepository;
import com.eunsil.bookmarky.repository.PassageRepository;
import com.eunsil.bookmarky.repository.user.UserRepository;
import com.eunsil.bookmarky.service.book.BookService;
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
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PassageService {

    private static final int DEFAULT_PASSAGE_SIZE = 10;

    private final FilterManager filterManager;
    private final UserRepository userRepository;
    private final BookService bookService;
    private final BookRepository bookRepository;
    private final PassageRepository passageRepository;
    private final SecurityUtil securityUtil;


    /**
     * 구절 생성
     * - DB에 없는 책의 경우, 구절 생성 시 새로운 책 정보가 함께 DB에 저장됨
     * @param passageVO isSaved, isbn, content
     * @return 생성 여부
     * @throws Exception 책 정보가 없을 때 검색을 위해 open api 호출 후 응답 값을 XML 로 변환하는 과정에서 발생 가능
     */
    @Transactional
    public boolean add(PassageVO passageVO) throws Exception {

        try {
            Long newBookId;
            if (bookRepository.existsByIsbn(passageVO.getIsbn())) {
                newBookId = bookRepository.findByIsbn(passageVO.getIsbn()).getId();
            } else {
                BookDTO bookDTO = bookService.searchByOpenApiWithIsbn(passageVO.getIsbn());
                newBookId = bookService.add(bookDTO);
            }

            Passage passage = Passage.builder()
                    .bookId(newBookId)
                    .userId(userRepository.findByUsername(securityUtil.getCurrentUsername()).getId())
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
        passage.setPageNum(passageUpdateVO.getPageNum());
        passage.setCreatedAt(LocalDate.now());

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
    public PassageDTO get(Long id) {
        Passage passage = passageRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Passage Not Found"));

        return PassageDTO.builder()
                .id(passage.getId())
                .userId(passage.getUserId())
                .bookId(passage.getBookId())
                .content(passage.getContent())
                .createdAt(passage.getCreatedAt())
                .build();
    }


    /**
     * 구절 목록 조회
     * - 저장한 구절을 전체적으로 조회하기 위함
     * - 페이징: passage id 기준 내림차순, 반환 개수 10개 고정
     * @param bookId 책 id
     * @param order 정렬 기준
     * @param page 페이지 번호
     * @return PassageDTO 리스트 (pageNum, content)
     */
    public List<PassageDTO> getList(Long bookId, String order, int page) {

        // 필터 활성화
        filterManager.enableFilter("deletedPassageFilter", "isDeleted", false);


        User user = userRepository.findByUsername(securityUtil.getCurrentUsername()); // Authentication 에서 가져옴
        Pageable pageable = PageRequest.of(page, DEFAULT_PASSAGE_SIZE, Sort.by(order).descending());
        Page<Passage> passagesList = passageRepository.findByUserIdAndBookId(user.getId(), bookId, pageable);

        // 필터 비활성화
        filterManager.disableFilter("deletedPassageFilter");


        return passagesList.stream()
                .map(passage -> new PassageDTO(
                        passage.getId()
                        , passage.getUserId()
                        , passage.getBookId()
                        , passage.getPageNum()
                        , passage.getContent()
                        , passage.getCreatedAt()))
                .collect(Collectors.toList());

    }


    /**
     * 최근 삭제 내역 조회 (30일 보관)
     *
     * @param page 페이지 번호
     * @return PassageDTO 리스트 (pageNum, content)
     */
    public List<PassageDTO> getAllDeleted(int page) {

        // 필터 활성화
        filterManager.enableFilter("deletedPassageFilter", "isDeleted", true);


        User user = userRepository.findByUsername(securityUtil.getCurrentUsername());
        Pageable pageable = PageRequest.of(page, DEFAULT_PASSAGE_SIZE, Sort.by("id").descending());
        Page<Passage> deletedPassageList = passageRepository.findByUserId(user.getId(), pageable);


        // 필터 비활성화
        filterManager.disableFilter("deletedPassageFilter");


        return deletedPassageList.stream()
                .map(passage -> new PassageDTO(
                        passage.getId()
                        , passage.getUserId()
                        , passage.getBookId()
                        , passage.getPageNum()
                        , passage.getContent()
                        , passage.getCreatedAt()))
                .collect(Collectors.toList());

    }


    /**
     * 삭제된 구절 복구
     * @param id 구절 id
     * @return 복구된 구절
     */
    public PassageDTO restoreDeletedPassage(Long id) {
        Passage passage = passageRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Passage Not Found"));
        passage.setIsDeleted(false);
        passageRepository.save(passage);

        return PassageDTO.builder()
                .id(passage.getId())
                .bookId(passage.getBookId())
                .userId(passage.getUserId())
                .content(passage.getContent())
                .pageNum(passage.getPageNum())
                .createdAt(passage.getCreatedAt())
                .build();
    }


    /**
     * 삭제한 지 30일 지난 구절 영구 제거
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void dailyCleanUpOfDeletedPassages() {
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        passageRepository.deleteByIsDeletedTrueAndDeletedAtBefore(thirtyDaysAgo);
        log.info("삭제된 지 30일 경과된 Passage 영구 제거");
    }

}
