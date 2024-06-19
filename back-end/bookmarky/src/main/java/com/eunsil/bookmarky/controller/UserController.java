package com.eunsil.bookmarky.controller;

import com.eunsil.bookmarky.config.auth.CustomUserDetailsService;
import com.eunsil.bookmarky.domain.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {

    private final CustomUserDetailsService userDetailService;

    @Autowired
    public UserController(CustomUserDetailsService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @PostMapping("/join")
    public String join(@RequestBody UserDTO userDTO) {
        return userDetailService.join(userDTO);
    }

}
