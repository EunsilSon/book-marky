package com.eunsil.bookmarky.controller;

import com.eunsil.bookmarky.domain.vo.PassageVO;
import com.eunsil.bookmarky.domain.vo.PassageUpdateVO;
import com.eunsil.bookmarky.domain.dto.PassageDTO;
import com.eunsil.bookmarky.response.ApiResponse;
import com.eunsil.bookmarky.response.ResponseUtil;
import com.eunsil.bookmarky.service.PassageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PassageController {

    private final PassageService passageService;

    @PostMapping("/passage")
    public ApiResponse<String> create(@Valid @RequestBody PassageVO passageVO) {
        if (passageService.createPassage(passageVO)) {
            return ResponseUtil.createSuccessResponse();
        }
        return ResponseUtil.createErrorResponse(HttpStatus.NOT_FOUND, "Book Not Found.");
    }

    @PatchMapping("/passage")
    public ApiResponse<String> update(@Valid @RequestBody PassageUpdateVO passageUpdateVO) {
        if (passageService.updatePassage(passageUpdateVO)) {
            return ResponseUtil.createSuccessResponse();
        }
        return ResponseUtil.createErrorResponse(HttpStatus.NOT_FOUND, "Passage Not Found.");
    }

    @DeleteMapping("/passage/{id}")
    public ApiResponse<String> delete(@PathVariable Long id) {
        if (passageService.deletePassage(id)) {
            return ResponseUtil.createSuccessResponse();
        }
        return ResponseUtil.createErrorResponse(HttpStatus.NOT_FOUND, "Passage Not Found.");
    }

    @GetMapping("/passage/{id}")
    public ApiResponse<PassageDTO> getPassageDetails(@PathVariable Long id) {
        return ResponseUtil.createSuccessResponse(passageService.getPassageDetails(id));
    }

    @GetMapping("/passages")
    public ApiResponse<List<PassageDTO>> getPassages(@RequestParam Long bookId, @RequestParam(defaultValue = "id") String order, @RequestParam(defaultValue = "0") int page) {
        return ResponseUtil.createSuccessResponse(passageService.getPassages(bookId, order, page));
    }

    @GetMapping("/passages/deleted")
    public ApiResponse<List<PassageDTO>> getDeletedPassages(@RequestParam(defaultValue = "0") int page) {
        return ResponseUtil.createSuccessResponse(passageService.getDeletedPassages(page));
    }

    @GetMapping("/passage/restore/{id}")
    public ApiResponse<String> restorePassage(@PathVariable String id) {
        if (passageService.restoreDeletedPassage(id)) {
            return ResponseUtil.createSuccessResponse();
        } else {
            return ResponseUtil.createErrorResponse(HttpStatus.NOT_FOUND, "Passage Not Found.");
        }
    }


}
