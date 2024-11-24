package com.eunsil.bookmarky.controller.user;

import com.eunsil.bookmarky.domain.vo.SecureQuestionVO;
import com.eunsil.bookmarky.global.response.ApiResponse;
import com.eunsil.bookmarky.global.response.ResponseUtil;
import com.eunsil.bookmarky.service.user.SecureQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class SecureQuestionController {

    private final SecureQuestionService secureQuestionService;

    @PostMapping("/user/question")
    public ApiResponse<String> checkQuestion(@RequestBody SecureQuestionVO secureQuestionVO) {
        if (secureQuestionService.checkQuestion(secureQuestionVO)) {
            return ResponseUtil.createSuccessResponse();
        }
        return ResponseUtil.createErrorResponse(HttpStatus.UNAUTHORIZED, "Incorrect Security Answer.");
    }

    @GetMapping("/user/question/{username}")
    public ApiResponse<String> getQuestion(@PathVariable String username) {
        return ResponseUtil.createSuccessResponse(secureQuestionService.getQuestion(username));
    }

}
