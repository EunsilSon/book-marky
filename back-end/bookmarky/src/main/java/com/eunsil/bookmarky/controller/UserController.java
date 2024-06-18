package com.eunsil.bookmarky.controller;

import com.eunsil.bookmarky.config.auth.CustomUserDetailsService;
import com.eunsil.bookmarky.domain.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class UserController {

    private final CustomUserDetailsService userDetailService;

    @Autowired
    public UserController(CustomUserDetailsService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @GetMapping("/")
    public String login() {
        return "ok";
    }

    @PostMapping("/join")
    public String join(@RequestBody UserDTO userDTO) {
        return userDetailService.join(userDTO);
    }

}
