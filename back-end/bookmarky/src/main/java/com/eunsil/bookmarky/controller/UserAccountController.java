package com.eunsil.bookmarky.controller;

import com.eunsil.bookmarky.domain.entity.SecureQuestion;
import com.eunsil.bookmarky.domain.vo.SecureQuestionVO;
import com.eunsil.bookmarky.domain.vo.PasswordVO;
import com.eunsil.bookmarky.service.user.UserAccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserAccountController {

    private final UserAccountService userAccountService;

    /**
     * 비밀번호 변경 메일 요청
     */
    @GetMapping("/user/mail/{username}")
    public ResponseEntity<String> sendResetEmail(@PathVariable String username) {
        if (userAccountService.sendResetEmailWithToken(username)) {
            return ResponseEntity.status(HttpStatus.OK).body("ok");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


    /**
     * 비밀번호 변경
     */
    @PostMapping("/user")
    public ResponseEntity<String> resetPw(@Valid @RequestBody PasswordVO passwordVO) {
        if (userAccountService.resetPwWithValidateToken(passwordVO)) {
            return ResponseEntity.status(HttpStatus.OK).body("ok");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token validation failed");
    }


    /**
     * 보안 질문 검증
     * :비밀번호 변경 시 토큰 탈취 방지를 위한 2차 검증
     */
    @PostMapping("/user/question")
    public ResponseEntity<String> checkSecureQuestion(@RequestBody SecureQuestionVO secureQuestionVO) {
        if (userAccountService.checkSecureQuestion(secureQuestionVO)) {
            return ResponseEntity.status(HttpStatus.OK).body("ok");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not matched secure answer");
    }


    /**
     * 보안 질문 조회
     */
    @GetMapping("/user/question/{username}")
    public ResponseEntity<String> getSecureQuestion(@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK).body(userAccountService.getSecureQuestion(username));
    }


    /**
     * 닉네임 조회
     */
    @GetMapping("/user/nickname")
    public ResponseEntity<String> getNickname() {
        String nickname = userAccountService.getNickname();
        if (nickname == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(nickname);
    }


}
