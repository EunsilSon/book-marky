package com.eunsil.bookmarky.controller;

import com.eunsil.bookmarky.domain.vo.PwResetVO;
import com.eunsil.bookmarky.domain.dto.PwResetDTO;
import com.eunsil.bookmarky.service.UserService;
import com.eunsil.bookmarky.domain.vo.UserVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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
    @GetMapping("/{username}")
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

}
