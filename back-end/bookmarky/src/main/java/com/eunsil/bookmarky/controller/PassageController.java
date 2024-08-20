package com.eunsil.bookmarky.controller;

import com.eunsil.bookmarky.domain.vo.PassageVO;
import com.eunsil.bookmarky.domain.vo.PassageUpdateVO;
import com.eunsil.bookmarky.domain.dto.PassageDTO;
import com.eunsil.bookmarky.service.passage.PassageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PassageController {

    private final PassageService passageService;

    /**
     * 구절 생성
     *
     * @param passageVO isbn, bookId, content
     * @return 생성 여부
     * @throws Exception 책 정보가 없을 때 검색을 위해 open api 호출 후 응답 값을 XML 로 변환하는 과정에서 발생 가능
     */
    @PostMapping("/passage")
    public ResponseEntity add(@Valid @RequestBody PassageVO passageVO) throws Exception {
        return ResponseEntity.ok(passageService.add(passageVO));
    }


    /**
     * 구절 수정
     *
     * @param passageUpdateVO isbn, bookId, content
     * @return 수정 여부
     */
    @PatchMapping("/passage")
    public ResponseEntity update(@Valid @RequestBody PassageUpdateVO passageUpdateVO) {
        return ResponseEntity.ok(passageService.update(passageUpdateVO));
    }


    /**
     * 구절 삭제
     *
     * @param id 구절 id
     * @return 삭제 여부
     */
    @DeleteMapping("/passage/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        return ResponseEntity.ok(passageService.delete(id));
    }


    /**
     * 구절 상세 조회
     *
     * @param id 유저 이메일
     * @return Passage 객체
     */
    @GetMapping("/passage/{id}")
    public ResponseEntity<PassageDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(passageService.get(id));
    }


    /**
     * 구절 목록 조회
     * - 저장한 구절을 전체적으로 조회하기 위함
     * @param bookId 책 id
     * @param type 정렬 기준
     * @param page 페이지 번호
     * @return PassageDTO 리스트
     */
    @GetMapping("/passages")
    public ResponseEntity<List<PassageDTO>> getList(@RequestParam Long bookId, @RequestParam(defaultValue = "id") String type, @RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(passageService.getList(bookId, type, page));
    }


    /**
     * 최근 삭제 내역 조회
     *
     * @param page 페이지 번호
     * @return PassageDTO 리스트 (pageNum, content)
     */
    @GetMapping("/passage/deleted")
    public ResponseEntity<List<PassageDTO>> getAllDeleted( @RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(passageService.getAllDeleted(page));
    }


}
