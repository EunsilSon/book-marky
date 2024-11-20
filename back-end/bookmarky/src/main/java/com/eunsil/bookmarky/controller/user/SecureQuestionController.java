package com.eunsil.bookmarky.controller.user;

import com.eunsil.bookmarky.domain.vo.SecureQuestionVO;
import com.eunsil.bookmarky.response.ApiResponse;
import com.eunsil.bookmarky.response.ResponseUtil;
import com.eunsil.bookmarky.service.user.SecureQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class SecureQuestionController {

    private final SecureQuestionService secureQuestionService;

    @PostMapping("/user/question")
    public ApiResponse<String> checkSecureQuestion(@RequestBody SecureQuestionVO secureQuestionVO) {
        if (secureQuestionService.checkSecureQuestion(secureQuestionVO)) {
            return ResponseUtil.createSuccessResponse();
        }
        return ResponseUtil.createErrorResponse(HttpStatus.UNAUTHORIZED, "Security Answer Not Matched.");
    }

    @GetMapping("/user/question/{username}")
    public ApiResponse<String> getSecureQuestion(@PathVariable String username) {
        return ResponseUtil.createSuccessResponse(secureQuestionService.getSecureQuestion(username));
    }

}
