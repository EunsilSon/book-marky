package com.eunsil.bookmarky.controller.user;

import com.eunsil.bookmarky.domain.vo.SecureQuestionVO;
import com.eunsil.bookmarky.service.user.SecureQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class SecureQuestionController {

    private final SecureQuestionService secureQuestionService;

    @PostMapping("/user/question")
    public ResponseEntity<String> checkSecureQuestion(@RequestBody SecureQuestionVO secureQuestionVO) {
        if (secureQuestionService.checkSecureQuestion(secureQuestionVO)) {
            return ResponseEntity.status(HttpStatus.OK).body("ok");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not matched secure answer");
    }

    @GetMapping("/user/question/{username}")
    public ResponseEntity<String> getSecureQuestion(@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK).body(secureQuestionService.getSecureQuestion(username));
    }

}
