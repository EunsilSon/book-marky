package com.eunsil.bookmarky.controller;

import com.eunsil.bookmarky.domain.entity.Passage;
import com.eunsil.bookmarky.domain.request.PassageReq;
import com.eunsil.bookmarky.domain.request.PassageUpdateReq;
import com.eunsil.bookmarky.service.PassageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/passage")
public class PassageController {

    private final PassageService passageService;

    public PassageController(PassageService passageService) {
        this.passageService = passageService;
    }

    /**
     * 구절 생성
     * @param passageReq isbn, bookId, username, content
     * @return 생성 여부
     * @throws Exception 책 정보가 없을 때 검색을 위해 open api 호출 후 응답 값을 XML 로 변환하는 과정에서 발생 가능
     */
    @PostMapping("/")
    public ResponseEntity add(@Valid @RequestBody PassageReq passageReq) throws Exception {
        return passageService.add(passageReq);
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
     * 구절 수정
     * @param passageUpdateReq isbn, bookId, username, content
     * @return 수정 여부
     */
    @PatchMapping("/")
    public ResponseEntity update(@Valid @RequestBody PassageUpdateReq passageUpdateReq) {
        return passageService.update(passageUpdateReq);
    }

}
