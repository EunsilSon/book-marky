package com.eunsil.bookmarky.controller;

import com.eunsil.bookmarky.service.UserService;
import com.eunsil.bookmarky.domain.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/join")
    public String join(@RequestBody UserDTO userDTO) {
        return userService.join(userDTO);
    }

}
