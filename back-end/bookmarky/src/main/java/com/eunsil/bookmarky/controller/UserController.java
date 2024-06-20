package com.eunsil.bookmarky.controller;

import com.eunsil.bookmarky.domain.dto.PasswordResetReq;
import com.eunsil.bookmarky.domain.dto.PasswordResetRes;
import com.eunsil.bookmarky.service.UserService;
import com.eunsil.bookmarky.domain.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 회원 가입
     * @param userDTO 이메일, 비밀번호, 닉네임
     * @return 성공 여부
     */
    @PostMapping("/")
    public boolean join(@RequestBody UserDTO userDTO) {
        return userService.join(userDTO);
    }


    /**
     * 비밀번호 변경 메일 요청
     * @param username 로그인에 사용한 이메일
     * @return 유저 이름, 일회용 토큰
     * @throws Exception 존재하지 않는 사용자
     */
    @GetMapping("/{username}")
    public PasswordResetRes sendResetEmail(@PathVariable String username) throws Exception {
        return userService.sendResetEmail(username);
    }

}
