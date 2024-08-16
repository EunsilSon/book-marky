package com.eunsil.bookmarky.controller;

import com.eunsil.bookmarky.domain.entity.SecureQuestion;
import com.eunsil.bookmarky.domain.vo.PwQuestionVO;
import com.eunsil.bookmarky.domain.vo.PwResetVO;
import com.eunsil.bookmarky.domain.dto.PwResetDTO;
import com.eunsil.bookmarky.exception.CommonResponse;
import com.eunsil.bookmarky.exception.ResponseService;
import com.eunsil.bookmarky.service.user.UserService;
import com.eunsil.bookmarky.domain.vo.UserVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;
    private final ResponseService responseService;

    @Autowired
    public UserController(UserService userService,ResponseService responseService) {
        this.userService = userService;
        this.responseService = responseService;
    }

    /**
     * 회원 가입
     * @param userVO 유저 이메일, 비밀번호, 닉네임
     * @return 성공 여부
     */
    @PostMapping("/")
    public boolean join(@Valid @RequestBody UserVO userVO) {
        return userService.join(userVO);
    }


    /**
     * 비밀번호 변경 메일 요청
     * @param username 유저 이메일
     * @return 유저 이메일, 일회용 토큰
     */
    @GetMapping("/{username}/mail")
    public ResponseEntity<PwResetDTO> sendResetEmail(@PathVariable String username) {
        return userService.sendResetEmailWithToken(username);
    }


    /**
     * 유효한 링크로 접속 후 비밀번호 변경
     * @param pwResetVO 유저 이메일, 비밀번호, 토큰
     * @return 변경 여부
     */
    @PutMapping("/")
    public boolean resetPw(@Valid @RequestBody PwResetVO pwResetVO) {
        return userService.resetPwWithTokenValidation(pwResetVO);
    }


    /**
     * 보안 질문 검증
     * - 비밀번호 변경 시 토큰 탈취 방지를 위한 2차 검증
     *
     * @param pwQuestionVO 유저 메일, 답변
     * @return 저장된 답변과 일치 여부
     */
    @PostMapping("/pw")
    public ResponseEntity<String> checkSecureQuestion(@Valid @RequestBody PwQuestionVO pwQuestionVO) {
        if (userService.checkSecureQuestion(pwQuestionVO)) {
            return ResponseEntity.status(HttpStatus.OK).body("success");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("failed");
    }


    /**
     * 보안 질문 조회
     *
     * @return SecureQuestionId 리스트
     */
    @GetMapping("/question")
    public ResponseEntity<List<SecureQuestion>> getSecureQuestion() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getSecureQuestion());

    }

}
