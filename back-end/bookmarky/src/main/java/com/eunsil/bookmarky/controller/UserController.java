package com.eunsil.bookmarky.controller;

import com.eunsil.bookmarky.domain.request.PasswordResetReq;
import com.eunsil.bookmarky.domain.response.PasswordResetRes;
import com.eunsil.bookmarky.service.UserService;
import com.eunsil.bookmarky.domain.dto.UserDTO;
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
     * @param userDTO 유저 이메일, 비밀번호, 닉네임
     * @return 성공 여부
     */
    @PostMapping("/")
    public boolean join(@Valid @RequestBody UserDTO userDTO) {
        return userService.join(userDTO);
    }


    /**
     * 비밀번호 변경 메일 요청
     * @param username 유저 이메일
     * @return 유저 이메일, 일회용 토큰
     * @throws Exception 존재하지 않는 사용자
     */
    @GetMapping("/{username}")
    public ResponseEntity<PasswordResetRes> sendResetEmail(@PathVariable String username) throws Exception {
        return userService.sendResetEmail(username);
    }


    /**
     * 유효한 링크로 접속 후 비밀번호 변경
     * @param passwordResetReq 유저 이메일, 비밀번호, 토큰
     * @return 변경 여부
     */
    @PutMapping("/")
    public boolean resetPw(@RequestBody PasswordResetReq passwordResetReq) {
        return userService.resetPw(passwordResetReq);
    }

}
