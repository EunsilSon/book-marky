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

    @PostMapping("/passage")
    public ResponseEntity<String> create(@Valid @RequestBody PassageVO passageVO) {
        if (passageService.createPassage(passageVO)) {
            return ResponseEntity.status(HttpStatus.OK).body("Ok");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found book");
    }

    @PatchMapping("/passage")
    public ResponseEntity<String> update(@Valid @RequestBody PassageUpdateVO passageUpdateVO) {
        if (passageService.updatePassage(passageUpdateVO)) {
            return ResponseEntity.status(HttpStatus.OK).body("Ok");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found Passage");
    }

    @DeleteMapping("/passage/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        if (passageService.deletePassage(id)) {
            return ResponseEntity.status(HttpStatus.OK).body("Ok");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found Passage");
    }

    @GetMapping("/passage/{id}")
    public ResponseEntity<PassageDTO> getPassageDetails(@PathVariable Long id) {
        return ResponseEntity.ok(passageService.getPassageDetails(id));
    }

    @GetMapping("/passages")
    public ResponseEntity<List<PassageDTO>> getPassages(@RequestParam Long bookId, @RequestParam(defaultValue = "id") String order, @RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(passageService.getPassages(bookId, order, page));
    }

    @GetMapping("/passages/deleted")
    public ResponseEntity<List<PassageDTO>> getDeletedPassages(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(passageService.getDeletedPassages(page));
    }

    @GetMapping("/passage/restore/{id}")
    public ResponseEntity<Void> restorePassage(@PathVariable String id) {
        if (passageService.restoreDeletedPassage(id)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


}
