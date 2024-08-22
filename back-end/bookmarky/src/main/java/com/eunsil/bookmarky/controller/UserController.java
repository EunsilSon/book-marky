package com.eunsil.bookmarky.controller;

import com.eunsil.bookmarky.domain.entity.SecureQuestion;
import com.eunsil.bookmarky.domain.vo.SecureQuestionVO;
import com.eunsil.bookmarky.domain.vo.PasswordVO;
import com.eunsil.bookmarky.domain.dto.PasswordDTO;
import com.eunsil.bookmarky.service.user.UserService;
import com.eunsil.bookmarky.domain.vo.UserVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    /**
     * 회원 가입
     * @param userVO 유저 이메일, 비밀번호, 닉네임
     * @return 성공 여부
     */
    @PostMapping("/user")
    public boolean join(@Valid @RequestBody UserVO userVO) {
        return userService.join(userVO);
    }


    /**
     * 비밀번호 변경 메일 요청
     * @param username 유저 이메일
     * @return 유저 이메일, 일회용 토큰
     */
    @GetMapping("/user/mail/{username}")
    public ResponseEntity<PasswordDTO> sendResetEmail(@PathVariable String username) {
        return userService.sendResetEmailWithToken(username);
    }


    /**
     * 유효한 링크로 접속 후 비밀번호 변경
     * @param passwordVO 유저 이메일, 비밀번호, 토큰
     * @return 변경 여부
     */
    @PutMapping("/user")
    public boolean resetPw(@Valid @RequestBody PasswordVO passwordVO) {
        return userService.resetPwWithTokenValidation(passwordVO);
    }


    /**
     * 보안 질문 검증
     * - 비밀번호 변경 시 토큰 탈취 방지를 위한 2차 검증
     *
     * @param secureQuestionVO 유저 메일, 답변
     * @return 저장된 답변과 일치 여부
     */
    @PostMapping("/user/question")
    public ResponseEntity<String> checkSecureQuestion(@Valid @RequestBody SecureQuestionVO secureQuestionVO) {
        if (userService.checkSecureQuestion(secureQuestionVO)) {
            return ResponseEntity.status(HttpStatus.OK).body("success");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("failed");
    }


    /**
     * 보안 질문 조회
     *
     * @return SecureQuestionId 리스트
     */
    @GetMapping("/user/question")
    public ResponseEntity<List<SecureQuestion>> getSecureQuestion() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getSecureQuestion());
    }

}
