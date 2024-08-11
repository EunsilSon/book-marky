package com.eunsil.bookmarky.controller;

import com.eunsil.bookmarky.domain.entity.Passage;
import com.eunsil.bookmarky.domain.vo.PassageVO;
import com.eunsil.bookmarky.domain.vo.PassageUpdateVO;
import com.eunsil.bookmarky.domain.dto.PassageListDTO;
import com.eunsil.bookmarky.service.PassageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/passage")
public class PassageController {

    private final PassageService passageService;

    public PassageController(PassageService passageService) {
        this.passageService = passageService;
    }

    /**
     * 구절 생성
     * @param passageVO isbn, bookId, username, content
     * @return 생성 여부
     * @throws Exception 책 정보가 없을 때 검색을 위해 open api 호출 후 응답 값을 XML 로 변환하는 과정에서 발생 가능
     */
    @PostMapping("/")
    public ResponseEntity add(@Valid @RequestBody PassageVO passageVO) throws Exception {
        return passageService.add(passageVO);
    }


    /**
     * 구절 수정
     * @param passageUpdateVO isbn, bookId, username, content
     * @return 수정 여부
     */
    @PatchMapping("/")
    public ResponseEntity update(@Valid @RequestBody PassageUpdateVO passageUpdateVO) {
        return passageService.update(passageUpdateVO);
    }


    /**
     * 구절 상세 조회
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<Passage> get(@PathVariable Long id) {
        return passageService.get(id);
    }


    /**
     * 구절 목록 조회
     * - 저장한 구절을 전체적으로 조회하기 위함
     * @param username
     * @param bookId
     * @return
     */
    @GetMapping("/{username}/{bookId}")
    public ResponseEntity<List<PassageListDTO>> getList(@PathVariable String username, @PathVariable Long bookId, @RequestParam(defaultValue = "0") int page) {
        return passageService.getList(username, bookId, page);
    }


    /**
     * 최근 삭제 내역 조회
     * @param username
     * @return 삭제한 구절 리스트
     */@GetMapping("/deleted/{username}")
    public List<Passage> getAllDeleted(@PathVariable String username) {
        return passageService.getAllDeleted(username);
    }


    /**
     * 구절 삭제
     * @param id 구절 id
     * @return 삭제 여부
     */
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        return passageService.delete(id);
    }


}
