package com.eunsil.bookmarky.controller;

import com.eunsil.bookmarky.domain.vo.PassageVO;
import com.eunsil.bookmarky.domain.vo.PassageUpdateVO;
import com.eunsil.bookmarky.domain.dto.PassageDTO;
import com.eunsil.bookmarky.service.PassageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PassageController {

    private final PassageService passageService;

    /**
     * 구절 생성
     */
    @PostMapping("/passage")
    public ResponseEntity<String> create(@Valid @RequestBody PassageVO passageVO) {
        if (passageService.createPassage(passageVO)) {
            return ResponseEntity.status(HttpStatus.OK).body("Ok");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found book");
    }


    /**
     * 구절 수정
     */
    @PatchMapping("/passage")
    public ResponseEntity<String> update(@Valid @RequestBody PassageUpdateVO passageUpdateVO) {
        if (passageService.update(passageUpdateVO)) {
            return ResponseEntity.status(HttpStatus.OK).body("Ok");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found Passage");
    }


    /**
     * 구절 삭제
     */
    @DeleteMapping("/passage/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        if (passageService.delete(id)) {
            return ResponseEntity.status(HttpStatus.OK).body("Ok");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found Passage");
    }


    /**
     * 구절 상세 조회
     */
    @GetMapping("/passage/{id}")
    public ResponseEntity<PassageDTO> getPassageDetails(@PathVariable Long id) {
        return ResponseEntity.ok(passageService.getPassageDetails(id));
    }


    /**
     * 구절 목록 조회
     */
    @GetMapping("/passages")
    public ResponseEntity<List<PassageDTO>> getPassages(@RequestParam Long bookId, @RequestParam(defaultValue = "id") String order, @RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(passageService.getPassages(bookId, order, page));
    }


    /**
     * 삭제한 구절 조회
     */
    @GetMapping("/passages/deleted")
    public ResponseEntity<List<PassageDTO>> getDeletedPassages(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(passageService.getDeletedPassages(page));
    }


    /**
     * 삭제한 구절 복구
     */
    @GetMapping("/passage/restore/{id}")
    public ResponseEntity<Void> restorePassage(@PathVariable String id) {
        if (passageService.restoreDeletedPassage(id)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


}
